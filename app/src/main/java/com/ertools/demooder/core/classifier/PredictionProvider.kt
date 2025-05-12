package com.ertools.demooder.core.classifier

import com.ertools.processing.commons.Emotion
import kotlinx.coroutines.flow.StateFlow

interface PredictionProvider {
    fun last(amount: Int): StateFlow<List<Prediction>>
    fun proportion(label: Emotion): StateFlow<Float>
    fun reset()
}