package com.ertools.processing.spectrogram

import com.ertools.processing.signal.Windowing

data class SpectrogramConfiguration(
    val frameSize: Int,
    val frameStep: Int,
    val windowing: Windowing.WindowType
)