package com.ertools.processing.dataset

import android.graphics.Bitmap
import android.graphics.Color
import com.ertools.processing.commons.ProcessingUtils.SPECTROGRAM_COLOR_RANGE
import com.ertools.processing.signal.SignalPreprocessor.convertStftToAmplitude
import org.jetbrains.kotlinx.multik.ndarray.complex.ComplexDoubleArray

class BitmapSpectrogram {
    lateinit var image: Bitmap

    companion object {
        fun fromComplexSpectrogram(complexData: Array<ComplexDoubleArray>): BitmapSpectrogram {
            val amplitudeData = complexData.convertStftToAmplitude()
            return fromAmplitudeSpectrogram(amplitudeData)

        }

        fun fromAmplitudeSpectrogram(amplitudeData: Array<DoubleArray>): BitmapSpectrogram {
            val width = amplitudeData.size
            val height = amplitudeData[0].size

            val maxIntensity = amplitudeData.maxOf { it.max() }
            val minIntensity = amplitudeData.minOf { it.min() }

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    val intensity = amplitudeData[x][y]
                    val normalizedIntensity = ((intensity - minIntensity) / (maxIntensity - minIntensity) * SPECTROGRAM_COLOR_RANGE).toFloat()
                    val color = Color.valueOf(normalizedIntensity, normalizedIntensity, normalizedIntensity)
                    bitmap.setPixel(x, height - y - 1, color.toArgb())
                }
            }
            return BitmapSpectrogram().apply { image = bitmap }
        }
    }
}