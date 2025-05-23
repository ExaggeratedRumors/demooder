package com.ertools.demooder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.audio.AudioProvider
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.demooder.core.classifier.PredictionRepository
import com.ertools.demooder.core.detector.DetectionProvider
import com.ertools.demooder.core.audio.AudioRecorder
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
import kotlin.math.min

class RecorderViewModel(
    private val audioProvider: AudioProvider,
    private val classifier: EmotionClassifier,
    private val detector: SpeechDetector,
    private val settingsStore: SettingsStore
) : ViewModel(), SpectrumProvider, DetectionProvider {
    /** Parameters **/
    private val recordingDelayMillis: Long = AppConstants.RECORDER_DELAY_MILLIS
    private val graphUpdatePeriodMillis: Long = AppConstants.UI_GRAPH_UPDATE_DELAY

    /** Data flows **/
    private val _spectrum = MutableStateFlow(OctavesAmplitudeSpectrum(ProcessingUtils.AUDIO_OCTAVES_AMOUNT))
    private val spectrum: StateFlow<OctavesAmplitudeSpectrum> = _spectrum.asStateFlow()

    private var _isSpeech = MutableStateFlow(false)
    private val isSpeech: StateFlow<Boolean> = _isSpeech.asStateFlow()

    private var _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()


    /**********/
    /** API **/
    /**********/
    fun toggleRecording() {
        if (isRecording.value) stopRecording()
        else viewModelScope.launch(Dispatchers.Main) { startRecording() }
    }

    fun saveRecording() {

    }

    fun clearRecording() {

    }

    override fun onCleared() {
        super.onCleared()
        if(isRecording.value) audioProvider.stop()
    }


    /*********************/
    /** Private methods **/
    /*********************/
    private suspend fun startRecording() {
        PredictionRepository.reset()
        val dataBufferSize = (settingsStore.signalDetectionPeriod.first() * ProcessingUtils.AUDIO_SAMPLING_RATE * 2).toInt()
        val dataBuffer = ByteArray(dataBufferSize)
        audioProvider.start()
        _isRecording.value = true

        viewModelScope.launch(Dispatchers.IO) {
            while(isActive && _isRecording.value) {
                synchronized(dataBuffer) {
                    audioProvider.read(dataBuffer)
                }
                delay(recordingDelayMillis)
            }
        }

        viewModelScope.launch {
            while(isActive && isRecording.value) {
                delay(graphUpdatePeriodMillis)
                val lastSampleSize = min(dataBuffer.size, audioProvider.getSampleRate())
                val data = dataBuffer.sliceArray(dataBufferSize - lastSampleSize until dataBufferSize)
                _spectrum.value = SpectrumBuilder.build(data)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val classificationPeriodMillis = (1000 * settingsStore.signalDetectionPeriod.first()).toLong()
            while(isActive && isRecording.value) {
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

    private fun stopRecording() {
        if(isRecording.value) audioProvider.stop()
        _isRecording.value = false
    }

    /********************/
    /** Implementation **/
    /********************/
    override fun getSpectrum(): StateFlow<OctavesAmplitudeSpectrum> = spectrum
    override fun isSpeech(): StateFlow<Boolean> = isSpeech
}


/** ViewModel Factory **/
class RecorderViewModelFactory(
    private val recorder: AudioRecorder,
    private val classifier: EmotionClassifier,
    private val detector: SpeechDetector,
    private val settingsStore: SettingsStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecorderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecorderViewModel(
                recorder,
                classifier,
                detector,
                settingsStore
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}