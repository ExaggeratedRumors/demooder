package com.ertools.processing.model

import com.ertools.processing.commons.Emotion
import java.nio.ByteBuffer

object InferenceStrategy {
    fun averageVoting(
        inputDataBuffers: List<ByteBuffer>,
        prediction: (ByteBuffer) -> Array<FloatArray>
    ): List<Pair<Emotion, Float>> {
        val labels = Emotion.entries.associateBy { it.ordinal }
        val votes = inputDataBuffers.size
        return inputDataBuffers.map { inputBuffer ->
            val result = prediction(inputBuffer)
            labels.map { (id, name) -> name to result.last()[id] }
        }.flatten()
            .groupBy({ it.first }, { it.second / votes })
            .mapValues{ it.value.sum() }
            .toList()
            .sortedByDescending { it.second }
    }
}