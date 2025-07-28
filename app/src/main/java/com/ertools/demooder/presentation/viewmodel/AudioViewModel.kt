package com.ertools.demooder.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.R
import com.ertools.demooder.core.audio.AudioProvider
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.classifier.PredictionRepository
import com.ertools.demooder.core.detector.DetectionProvider
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.demooder.core.notifications.MediaService
import com.ertools.demooder.core.notifications.NotificationAction
import com.ertools.demooder.core.notifications.NotificationData
import com.ertools.demooder.core.notifications.NotificationEventStream
import com.ertools.demooder.core.settings.SettingsStore
import com.ertools.demooder.core.spectrum.SpectrumBuilder
import com.ertools.demooder.core.spectrum.SpectrumProvider
import com.ertools.demooder.utils.AppConstants
import com.ertools.processing.commons.OctavesAmplitudeSpectrum
import com.ertools.processing.commons.ProcessingUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * ViewModel for managing current audio data buffer.
 */
class AudioViewModel(
    private val audioProvider: AudioProvider,
    private val classifier: EmotionClassifier,
    private val detector: SpeechDetector,
    private val settingsStore: SettingsStore
) : ViewModel(), SpectrumProvider, DetectionProvider {
    /** Parameters **/
    private val recordingDelayMillis: Long = AppConstants.RECORDER_DELAY_MILLIS
    private val graphUpdatePeriodMillis: Long = AppConstants.UI_GRAPH_UPDATE_DELAY

    /** Data flows **/
    private val _spectrum: MutableStateFlow<OctavesAmplitudeSpectrum> = MutableStateFlow(OctavesAmplitudeSpectrum(ProcessingUtils.AUDIO_OCTAVES_AMOUNT))
    private val spectrum: StateFlow<OctavesAmplitudeSpectrum> = _spectrum.asStateFlow()

    private var _isSpeech: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val isSpeech: StateFlow<Boolean> = _isSpeech.asStateFlow()

    private var _isWorking: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isWorking: StateFlow<Boolean> = _isWorking.asStateFlow()


    /**********/
    /** API **/
    /**********/

    fun runNotificationListeningTask(context: Context) {
        startBackgroundTask(context, AppConstants.NOTIFICATION_ACTION_NOTIFY)
        viewModelScope.launch {
            NotificationEventStream.events.collect { notificationData ->
                when(notificationData.action) {
                    NotificationAction.START -> startRecording()
                    NotificationAction.STOP -> stopRecording()
                    else -> {}
                }
            }
        }
    }

    fun togglePlay(context: Context) {
        viewModelScope.launch {
            val notificationData = if(isWorking.value) {
                NotificationData(
                    action = NotificationAction.STOP,
                    title = context.getString(R.string.prediction_placeholder)
                )
            } else {
                NotificationData(
                    action = NotificationAction.START,
                    title = context.getString(R.string.prediction_result_label),
                    subtitle = context.getString(R.string.prediction_result_loading)
                )
            }
            updateBackgroundTask(notificationData)
        }
    }

    fun save() {

    }

    fun abort() {

    }

    /*********************/
    /** Private methods **/
    /*********************/

    /**
     * Start recording audio and processing it.
     */
    private suspend fun startRecording() {
        /** Initialize audio provider **/
        PredictionRepository.reset()
        val dataBufferSize = (settingsStore.signalDetectionPeriod.first() * ProcessingUtils.AUDIO_SAMPLING_RATE * 2).toInt()
        val dataBuffer = ByteArray(dataBufferSize)
        audioProvider.start()
        _isWorking.value = true


        /** Read buffer from audio provider **/
        viewModelScope.launch(Dispatchers.IO) {
            while(isActive && isWorking.value) {
                synchronized(dataBuffer) {
                    audioProvider.read(dataBuffer)
                }
                delay(recordingDelayMillis)
            }
        }

        /** Update spectrum **/
        viewModelScope.launch {
            while(isActive && isWorking.value) {
                delay(graphUpdatePeriodMillis)
                val lastSampleSize = dataBuffer.size.coerceAtMost(AppConstants.UI_GRAPH_MAX_DATA_SIZE)
                val data = dataBuffer.sliceArray(dataBufferSize - lastSampleSize until dataBufferSize)
                _spectrum.value = SpectrumBuilder.build(data)
            }
        }

        /** Detect speech and classify emotions **/
        viewModelScope.launch(Dispatchers.IO) {
            val classificationPeriodMillis = (1000 * settingsStore.signalDetectionPeriod.first()).toLong()
            while(isActive && isWorking.value) {
                delay(classificationPeriodMillis)
                detector.detectSpeech(dataBuffer, audioProvider.getSampleRate()) { isSpeech ->
                    if(isSpeech) {
                        _isSpeech.value = true
                        classifier.predict(dataBuffer, audioProvider.getSampleRate()) { prediction ->
                            PredictionRepository.updatePredictions(prediction)
                            val notificationData = NotificationData(
                                action = NotificationAction.UPDATE,
                                title = prediction[0].label.name,
                                subtitle = "temp"
                            )
                            updateBackgroundTask(notificationData)
                        }
                    } else {
                        _isSpeech.value = false
                    }
                }
            }
        }
    }

    /**
     * Stop recording audio and processing it.
     */
    private fun stopRecording() {
        if(isWorking.value) audioProvider.stop()
        _isWorking.value = false
    }

    /**
     * Set background task for audio service.
     */
    private fun startBackgroundTask(context: Context, action: String, data: NotificationData? = null) {
        val serviceIntent = Intent(context, MediaService::class.java).apply {
            this.action = action
            this.putExtra(AppConstants.NOTIFICATION_DATA, data)
        }
        Log.d("AudioViewModel", "Starting service with action: $action, data: $data")
        ContextCompat.startForegroundService(context, serviceIntent)
    }

    private fun updateBackgroundTask(notificationData: NotificationData) {
        NotificationEventStream.events.tryEmit(notificationData)
    }

    /**
     * Stop background task for audio service.
     */
    private fun stopBackgroundTask() {
        val notificationData = NotificationData(action = NotificationAction.DESTROY)
        NotificationEventStream.events.tryEmit(notificationData)
    }

    /********************/
    /** Implementation **/
    /********************/
    override fun getSpectrum(): StateFlow<OctavesAmplitudeSpectrum> = spectrum
    override fun isSpeech(): StateFlow<Boolean> = isSpeech
    override fun onCleared() {
        super.onCleared()
        stopBackgroundTask()
        stopRecording()
    }
}

class AudioViewModelFactory(
    private val audioProvider: AudioProvider,
    private val classifier: EmotionClassifier,
    private val detector: SpeechDetector,
    private val settingsStore: SettingsStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AudioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AudioViewModel(
                audioProvider,
                classifier,
                detector,
                settingsStore
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}