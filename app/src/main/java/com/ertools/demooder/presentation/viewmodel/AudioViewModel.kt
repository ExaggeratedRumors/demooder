package com.ertools.demooder.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.audio.AudioProvider
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.classifier.PredictionRepository
import com.ertools.demooder.core.detector.DetectionProvider
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.demooder.core.settings.SettingsStore
import com.ertools.demooder.core.spectrum.SpectrumBuilder
import com.ertools.demooder.core.spectrum.SpectrumProvider
import com.ertools.demooder.presentation.components.interfaces.Resetable
import com.ertools.demooder.utils.AppConstants
import com.ertools.processing.commons.OctavesAmplitudeSpectrum
import com.ertools.processing.commons.ProcessingUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * ViewModel for managing current audio data buffer.
 * Modifies PredictionRepository.
 * Listen audio provider.
 */
class AudioViewModel(
    private val audioProvider: AudioProvider,
    private val classifier: EmotionClassifier,
    private val detector: SpeechDetector,
    private val settingsStore: SettingsStore
) : ViewModel(), SpectrumProvider, DetectionProvider, Resetable {
    /** Main buffer **/
    private var dataBuffer: ByteArray = ByteArray(0)

    /** Parameters **/
    private val recordingDelayMillis: Long = AppConstants.RECORDER_DELAY_MILLIS
    private val graphUpdatePeriodMillis: Long = AppConstants.UI_GRAPH_UPDATE_DELAY

    /** Data flows **/
    private val _spectrum: MutableStateFlow<OctavesAmplitudeSpectrum> = MutableStateFlow(OctavesAmplitudeSpectrum(ProcessingUtils.AUDIO_OCTAVES_AMOUNT))
    private val spectrum: StateFlow<OctavesAmplitudeSpectrum> = _spectrum.asStateFlow()

    private var _isSpeech: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val isSpeech: StateFlow<Boolean> = _isSpeech.asStateFlow()

    private val isWorking: StateFlow<Boolean> = audioProvider.isRunning()

    private val _errors: MutableSharedFlow<String> = MutableSharedFlow()
    val audioErrors = _errors.asSharedFlow()

    /**********/
    /** API **/
    /**********/

    fun runTasks() {
        startListeningTask()
    }

    /*********************/
    /** Private methods **/
    /*********************/

    /**
     * Start listening for audio data and processing it in the background.
     */
    private fun startListeningTask() {
        viewModelScope.launch {
            isWorking.collect { running ->
                if(running) start()
            }
        }
    }

    /**
     * Start recording audio and processing it.
     */
    private suspend fun start() {
        PredictionRepository.reset()
        val sampleRate = audioProvider.getSampleRate()
        if(sampleRate == null) {
            Log.e("AudioViewModel", "Sample rate is null, cannot start recording.")
            _errors.emit("Sample rate is null, cannot start recording.")
            return
        }
        val dataBufferSize = (settingsStore.signalDetectionPeriod.first() * sampleRate * 2).toInt()
        dataBuffer = ByteArray(dataBufferSize)
        startBufferReadingTask(dataBuffer)
        startSpectrumBuildingTask(dataBuffer, dataBufferSize)
        startEmotionDetectingTask(dataBuffer)
    }

    /**
     * Start a background task to read audio data into the provided buffer.
     * @param dataBuffer The byte array buffer to read audio data into.
     */
    private fun startBufferReadingTask(dataBuffer: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            while(isActive && isWorking.value) {
                synchronized(dataBuffer) {
                    audioProvider.read(dataBuffer)
                }
                delay(recordingDelayMillis)
            }
        }
    }

    /**
     * Start a background task to build the spectrum from the audio data buffer.
     * This task runs in a loop, updating the spectrum at regular intervals.
     * @param dataBuffer The byte array buffer containing audio data.
     */
    private fun startSpectrumBuildingTask(dataBuffer: ByteArray, dataBufferSize: Int) {
        viewModelScope.launch {
            while(isActive && isWorking.value) {
                delay(graphUpdatePeriodMillis)
                val lastSampleSize = dataBuffer.size.coerceAtMost(AppConstants.UI_GRAPH_MAX_DATA_SIZE)
                val data = dataBuffer.sliceArray(dataBufferSize - lastSampleSize until dataBufferSize)
                _spectrum.value = SpectrumBuilder.build(data)
            }
        }
    }


    /**
     * Start a background task to detect speech and classify emotions.
     * This task runs in a loop, checking the audio data buffer at regular intervals.
     * @param dataBuffer The byte array buffer containing audio data.
     */
    private fun startEmotionDetectingTask(dataBuffer: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            val classificationPeriodMillis = (1000 * settingsStore.signalDetectionPeriod.first()).toLong()
            while(isActive && isWorking.value) {
                delay(classificationPeriodMillis)
                audioProvider.getSampleRate()?.let { sampleRate ->
                    detector.detectSpeech(dataBuffer, sampleRate) { isSpeech ->
                        if(isSpeech) {
                            _isSpeech.value = true
                            classifier.predict(dataBuffer, sampleRate) { prediction ->
                                PredictionRepository.updatePredictions(prediction)
                            }
                        } else {
                            _isSpeech.value = false
                        }
                    }
                } ?: run {
                    Log.e("AudioViewModel", "Sample rate is null, cannot detect speech.")
                    _errors.emit("Sample rate is null, cannot detect speech.")
                }

            }
        }
    }
    /**
     * Stop recording audio and processing it.
     */
    private fun stopRecording() {
        if(isWorking.value) audioProvider.stop()
    }

    /********************/
    /** Implementation **/
    /********************/
    override fun getSpectrum(): StateFlow<OctavesAmplitudeSpectrum> = spectrum
    override fun isSpeech(): StateFlow<Boolean> = isSpeech
    override fun reset() { dataBuffer = ByteArray(0) }
    override fun onCleared() {
        super.onCleared()
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