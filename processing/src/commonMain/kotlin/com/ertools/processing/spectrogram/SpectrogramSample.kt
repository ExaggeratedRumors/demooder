package com.ertools.processing.spectrogram

import com.ertools.processing.commons.Spectrogram

data class SpectrogramSample(
    val data: Spectrogram,
    val filename: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpectrogramSample

        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + filename.hashCode()
        return result
    }

}