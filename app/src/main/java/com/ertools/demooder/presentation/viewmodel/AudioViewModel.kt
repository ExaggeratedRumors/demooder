package com.ertools.demooder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.audio.AudioProvider
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.classifier.PredictionRepository
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.demooder.core.detector.DetectionProvider
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

    fun togglePlay() {
        if (isWorking.value) stopRecording()
        else viewModelScope.launch(Dispatchers.IO) { startRecording() }
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
        PredictionRepository.reset()
        val dataBufferSize = (settingsStore.signalDetectionPeriod.first() * ProcessingUtils.AUDIO_SAMPLING_RATE * 2).toInt()

        val dataBuffer = ByteArray(dataBufferSize)
        audioProvider.start()
        _isWorking.value = true

        /** Read buffer from audio provider **/
        viewModelScope.launch(Dispatchers.IO) {
            while(isActive && _isWorking.value) {
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
                //val lastSampleSize = min(dataBuffer.size, audioProvider.getSampleRate())
                val lastSampleSize = 2048

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

    /********************/
    /** Implementation **/
    /********************/
    override fun getSpectrum(): StateFlow<OctavesAmplitudeSpectrum> = spectrum
    override fun isSpeech(): StateFlow<Boolean> = isSpeech
    override fun onCleared() {
        super.onCleared()
        if(isWorking.value) audioProvider.stop()
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