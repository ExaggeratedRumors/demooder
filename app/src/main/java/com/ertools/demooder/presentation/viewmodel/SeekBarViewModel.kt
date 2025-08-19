package com.ertools.demooder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.audio.AudioRunningProvider
import com.ertools.demooder.core.audio.ProgressProvider
import com.ertools.demooder.utils.AppConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing seek bar position and duration.
 * Listens audio running provider.
 * Listens progress provider.
 */
class SeekBarViewModel(
    private val progressProvider: ProgressProvider,
    private val audioProvider: AudioRunningProvider
): ViewModel() {
    /****************/
    /** Data flows **/
    /****************/
    private val _duration: MutableStateFlow<Int> = MutableStateFlow(progressProvider.getSize())
    val duration: StateFlow<Int> = _duration

    private val _position: MutableStateFlow<Int> = MutableStateFlow(0)
    val position: StateFlow<Int> = _position

    private val _isWorking: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isWorking: StateFlow<Boolean> = _isWorking


    /*********************/
    /** Public methods **/
    /*********************/

    /**
     * Run tasks to listen for audio data and update position.
     */
    fun runTasks() {
        runListeningTask()
        runPositionUpdatingTask()
    }

    /**
     * Start listening for audio data and processing it in the background.
     * @param ms position in milliseconds to start playback from.
     */
    fun seekTo(ms: Int) {
        progressProvider.seekTo(ms)
        _position.value = ms
    }


    /*********************/
    /** Private methods **/
    /*********************/

    /**
     * Start listening for audio running state and update isWorking state.
     * Emits NotificationAction.START or NotificationAction.STOP based on the state.
     */
    private fun runListeningTask() {
        viewModelScope.launch {
            audioProvider.isRunning().collect {
                _isWorking.value = it
            }
        }
    }

    /**
     * Run a background task to update the position of the seek bar.
     * This task runs every [AppConstants.UI_SEEK_BAR_UPDATE_DELAY] milliseconds.
     */
    private fun runPositionUpdatingTask() {
        viewModelScope.launch {
            while (isWorking.value) {
                _position.value = progressProvider.getCurrentPosition()
                delay(AppConstants.UI_SEEK_BAR_UPDATE_DELAY)
            }
        }
    }
}


class SeekBarViewModelFactory(
    private val progressProvider: ProgressProvider,
    private val audioProvider: AudioRunningProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SeekBarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SeekBarViewModel(progressProvider, audioProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}