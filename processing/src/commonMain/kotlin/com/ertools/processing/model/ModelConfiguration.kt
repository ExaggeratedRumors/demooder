package com.ertools.processing.model

import com.ertools.processing.signal.Windowing

data class ModelConfiguration(
    val modelName: String,
    val frameSize: Int,
    val frameStep: Int,
    val windowing: Windowing.WindowType,
    val threadCount: Int,
    val useNNAPI: Boolean
)

