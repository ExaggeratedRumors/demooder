package com.ertools.demooder.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.classifier.PredictionProvider
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.demooder.core.classifier.Prediction
import com.ertools.demooder.core.classifier.PredictionHistory
import com.ertools.demooder.core.recorder.AudioRecorder
import com.ertools.demooder.core.settings.SettingsPreferences
import com.ertools.demooder.core.settings.datastore
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

class RecorderViewModel(
    application: Application,
    private val recorder: AudioRecorder,
    private val classifier: EmotionClassifier,
    private val detector: SpeechDetector,
) : ViewModel(), PredictionProvider, SpectrumProvider {
    private val dataStore = application.datastore

    /** Parameters **/
    private val recordingDelayMillis: Long = AppConstants.RECORDER_DELAY_MILLIS
    private val recordingPeriodSeconds: Double = AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_SECONDS
    private val graphUpdatePeriodMillis: Long = AppConstants.UI_GRAPH_UPDATE_DELAY
    private val dataBufferSize = (recordingPeriodSeconds * ProcessingUtils.AUDIO_SAMPLING_RATE * 2).toInt()
    private val dataBuffer = ByteArray(dataBufferSize)

    /** Data flows **/
    private val _prediction = MutableStateFlow(emptyList<Prediction>())
    private val prediction: StateFlow<List<Prediction>> = _prediction.asStateFlow()

    private val _spectrum = MutableStateFlow(OctavesAmplitudeSpectrum(ProcessingUtils.AUDIO_OCTAVES_AMOUNT))
    private val spectrum: StateFlow<OctavesAmplitudeSpectrum> = _spectrum.asStateFlow()

    private var _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    /**********/
    /** API **/
    /**********/
    fun toggleRecording() {
        if (isRecording.value) stopRecording()
        else startRecording()
    }

    fun saveRecording() {

    }

    fun clearRecording() {

    }

    override fun onCleared() {
        super.onCleared()
        if(isRecording.value) recorder.stop()
    }


    /*********************/
    /** Private methods **/
    /*********************/
    private fun startRecording() {
        recorder.start()
        _isRecording.value = true

        viewModelScope.launch(Dispatchers.IO) {
            while(isActive && _isRecording.value) {
                synchronized(dataBuffer) {
                    recorder.read(dataBuffer)
                }
                delay(recordingDelayMillis)
            }
        }

        viewModelScope.launch {
            while(isActive && isRecording.value) {
                delay(graphUpdatePeriodMillis)
                val data = dataBuffer.sliceArray(dataBufferSize - recorder.recorderBufferSize until dataBufferSize)
                _spectrum.value = SpectrumBuilder.build(data)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val classificationPeriodMillis = (
                1000 * dataStore.data.first()[SettingsPreferences.SIGNAL_DETECTION_PERIOD]!!
            ).toLong()

            while(isActive && isRecording.value) {
                delay(classificationPeriodMillis)
                detector.detectSpeech(dataBuffer, recorder.sampleRate) { isSpeech ->
                    if(isSpeech) {
                        classifier.predict(dataBuffer, recorder.sampleRate) { prediction ->
                            PredictionHistory.updatePredictions(prediction)
                        }
                    } else {
                        _prediction.value = emptyList()
                    }
                }
            }
        }
    }

    private fun stopRecording() {
        if(isRecording.value) recorder.stop()
        _isRecording.value = false
    }

    /********************/
    /** Implementation **/
    /********************/
    override fun getPrediction(): StateFlow<List<Prediction>> = prediction
    override fun getSpectrum(): StateFlow<OctavesAmplitudeSpectrum> = spectrum
}


/** ViewModel Factory **/
class RecorderViewModelFactory(
    private val application: Application,
    private val recorder: AudioRecorder,
    private val classifier: EmotionClassifier,
    private val detector: SpeechDetector,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecorderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecorderViewModel(
                application,
                recorder,
                classifier,
                detector
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}