package com.ertools.processing.spectrogram

import com.ertools.processing.commons.Emotion
import com.ertools.processing.commons.Spectrogram

data class SpectrogramSample(
    val data: Spectrogram,
    val filename: String,
    val labels: Emotion?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpectrogramSample

        if (!data.contentEquals(other.data)) return false
        if (labels != other.labels) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + labels.hashCode()
        return result
    }
}