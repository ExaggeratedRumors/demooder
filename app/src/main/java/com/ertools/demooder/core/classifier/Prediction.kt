package com.ertools.demooder.core.classifier

import com.ertools.processing.commons.Emotion

data class Prediction(
    val label: Emotion,
    val confidence: Float
)
