package com.ertools.demooder.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ertools.demooder.core.audio.AudioProvider
import com.ertools.demooder.core.audio.AudioRecorder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Shared ViewModel for managing current provider state between PredictionView and RecordsView.
 */
class ProviderViewModel(
    application: Application
) : AndroidViewModel(application) {
    /** Data flow **/
    private var _currentProvider: MutableStateFlow<AudioProvider> = MutableStateFlow(AudioRecorder())
    val currentProvider: StateFlow<AudioProvider> = _currentProvider.asStateFlow()

    /** API **/
    fun updateCurrentProvider(provider: AudioProvider) {
        _currentProvider.value = provider
    }
}
