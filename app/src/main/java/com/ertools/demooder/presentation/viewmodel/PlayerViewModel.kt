package com.ertools.demooder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.classifier.PredictionRepository
import com.ertools.demooder.core.detector.DetectionProvider
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.demooder.core.player.AudioPlayer
import com.ertools.demooder.core.player.PlayerState
import com.ertools.demooder.core.settings.SettingsStore
import com.ertools.processing.commons.ProcessingUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val audioPlayer: AudioPlayer,
    private val classifier: EmotionClassifier,
    private val detector: SpeechDetector,
    private val settingsStore: SettingsStore
): ViewModel(), DetectionProvider {
    private var _isPlaying: MutableStateFlow<PlayerState> = MutableStateFlow(PlayerState.STOPPED)
    val isPlaying: StateFlow<PlayerState> = _isPlaying

    private var _isSpeech = MutableStateFlow(false)
    private val isSpeech: StateFlow<Boolean> = _isSpeech.asStateFlow()

    /**********/
    /** API **/
    /**********/

    fun play(audioFilePath: String) {

        audioPlayer.start(audioFilePath)
        _isPlaying.value = PlayerState.PLAYING
    }

    fun pause() {
        audioPlayer.pause()
        _isPlaying.value = PlayerState.PAUSED
    }

    fun stop() {
        audioPlayer.stop()
        _isPlaying.value = PlayerState.STOPPED
    }

    /*********************/
    /** Private methods **/
    /*********************/


    private suspend fun startPlayer(audioFilePath: String) {
        val dataBufferSize = (settingsStore.signalDetectionPeriod.first() * ProcessingUtils.AUDIO_SAMPLING_RATE * 2).toInt()
        val dataBuffer = ByteArray(dataBufferSize)
        audioPlayer.start(audioFilePath)
        _isPlaying.value = PlayerState.PLAYING

        viewModelScope.launch(Dispatchers.IO) {
            while(isActive && isPlaying.value == PlayerState.PLAYING) {
                synchronized(dataBuffer) {
                    audioPlayer.getCurrentAudio(dataBuffer)
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val classificationPeriodMillis = (1000 * settingsStore.signalDetectionPeriod.first()).toLong()
            while(isActive && isPlaying.value == PlayerState.PLAYING) {
                delay(classificationPeriodMillis)
                detector.detectSpeech(dataBuffer, audioPlayer.getSampleRate()) { isSpeech ->
                    if(isSpeech) {
                        _isSpeech.value = true
                        classifier.predict(dataBuffer, audioPlayer.getSampleRate()) { prediction ->
                            PredictionRepository.updatePredictions(prediction)
                        }
                    } else {
                        _isSpeech.value = false
                    }
                }
            }
        }
    }

    /********************/
    /** Implementation **/
    /********************/
    override fun isSpeech(): StateFlow<Boolean> = isSpeech
    override fun onCleared() {
        super.onCleared()
        stop()
    }
}