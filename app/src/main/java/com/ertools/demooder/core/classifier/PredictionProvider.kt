package com.ertools.demooder.core.classifier

import com.ertools.demooder.presentation.components.interfaces.Resetable
import com.ertools.processing.commons.Emotion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PredictionProvider: Resetable {
    fun last(amount: Int): StateFlow<List<Prediction>>
    fun count(emotion: Emotion): StateFlow<Int>
    fun statistics(): Flow<Map<Emotion, Int>>
    override fun reset()
}