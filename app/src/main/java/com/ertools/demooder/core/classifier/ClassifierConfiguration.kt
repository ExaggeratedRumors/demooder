package com.ertools.demooder.core.classifier

import com.ertools.processing.signal.Windowing

data class ClassifierConfiguration(
    val modelName: String,
    val frameSize: Int,
    val frameStep: Int,
    val windowing: Windowing.WindowType
)
