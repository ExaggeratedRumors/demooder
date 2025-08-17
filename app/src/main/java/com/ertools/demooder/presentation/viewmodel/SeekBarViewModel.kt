package com.ertools.demooder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.audio.ProgressProvider
import com.ertools.demooder.utils.AppConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SeekBarViewModel(
    private val progressProvider: ProgressProvider
): ViewModel() { private val _duration: MutableStateFlow<Int> = MutableStateFlow(progressProvider.getSize())
    val duration: StateFlow<Int> = _duration

    private val _position: MutableStateFlow<Int> = MutableStateFlow(0)
    val position: StateFlow<Int> = _position

    private val _isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    fun togglePlay() {
        _isPlaying.value = !isPlaying.value
        if(isPlaying.value) {
            viewModelScope.launch {
                while (isPlaying.value) {
                    _position.value = progressProvider.getCurrentPosition()
                    delay(AppConstants.UI_SEEK_BAR_UPDATE_DELAY)
                }
            }
        }
    }

    fun seekTo(ms: Int) {
        progressProvider.seekTo(ms)
        _position.value = ms
    }
}

class SeekBarViewModelFactory(
    private val progressProvider: ProgressProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SeekBarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SeekBarViewModel(progressProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}