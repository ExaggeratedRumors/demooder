package com.ertools.processing.model

import com.ertools.processing.commons.Emotion
import java.nio.ByteBuffer

object InferenceStrategy {
    fun majorityVoting(
        inputDataBuffers: List<ByteBuffer>,
        prediction: (ByteBuffer) -> Array<FloatArray>
    ): List<Pair<Emotion, Float>> {
        val labels = Emotion.entries.associateBy { it.ordinal }
        val modelsAmount = inputDataBuffers.size
        return inputDataBuffers.map { inputBuffer ->
            val result = prediction(inputBuffer)
            labels.map { (id, name) -> name to result.last()[id] }
        }.flatten()
            .groupBy({ it.first }, { it.second / modelsAmount })
            .mapValues{ it.value.sum() }
            .toList()
            .sortedByDescending { it.second }
    }
}