package com.ertools.processing.spectrogram

import com.ertools.processing.commons.ProcessingUtils.SPECTROGRAM_COLOR_RANGE
import com.ertools.processing.commons.Spectrogram
import com.ertools.processing.signal.SignalPreprocessor.convertStftToAmplitude
import java.awt.Color
import java.awt.image.BufferedImage

object SpectrogramImage {
    /**
     * Convert a Spectrogram to a BufferedImage.
     * @param spectrogram The Spectrogram to convert.
     * @return A BufferedImage representing the Spectrogram.
     */
    fun fromSpectrogram(spectrogram: Spectrogram): BufferedImage {
        val magnitudeMatrix = spectrogram.convertStftToAmplitude()

        /* Create image */
        val width = magnitudeMatrix.size
        val height = magnitudeMatrix[0].size

        val maxIntensity = magnitudeMatrix.maxOf { it.max() }
        val minIntensity = magnitudeMatrix.minOf { it.min() }

        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val intensity = magnitudeMatrix[x][y]
                val normalizedIntensity = ((intensity - minIntensity) / (maxIntensity - minIntensity) * SPECTROGRAM_COLOR_RANGE).toInt()
                val color = Color(normalizedIntensity, normalizedIntensity, normalizedIntensity).rgb
                image.setRGB(x, height - y - 1, color)
            }
        }
        return image
    }
}