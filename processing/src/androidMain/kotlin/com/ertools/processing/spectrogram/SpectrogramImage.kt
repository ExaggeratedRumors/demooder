package com.ertools.processing.spectrogram

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Environment
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import androidx.core.graphics.set
import com.ertools.processing.commons.AmplitudeSpectrum
import com.ertools.processing.commons.ProcessingUtils.SPECTROGRAM_COLOR_RANGE
import com.ertools.processing.commons.Spectrogram
import com.ertools.processing.model.ModelShape
import com.ertools.processing.signal.SignalPreprocessor.convertStftToAmplitude
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

object SpectrogramImage {
    /**
     * Convert a Spectrogram to a Bitmap image.
     * @param spectrogram The Spectrogram to convert.
     * @return A Bitmap image representing the Spectrogram.
     */
    fun fromSpectrogram(spectrogram: Spectrogram): Bitmap {
        val amplitudeData: Array<AmplitudeSpectrum> = spectrogram.convertStftToAmplitude()
        return fromAmplitudeSpectrum(amplitudeData)
    }

    /**
     * Convert an AmplitudeSpectrum to a Bitmap image.
     * @param amplitudeData The AmplitudeSpectrum to convert.
     * @return A Bitmap image representing the AmplitudeSpectrum.
     */
    fun fromAmplitudeSpectrum(amplitudeData: Array<AmplitudeSpectrum>): Bitmap {
        val width = amplitudeData.size
        val height = amplitudeData[0].size

        val maxIntensity = amplitudeData.maxOf { it.max() }
        val minIntensity = amplitudeData.minOf { it.min() }

        val bitmap = createBitmap(width, height)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val intensity = amplitudeData[x][y]
                val normalizedIntensity = ((intensity - minIntensity) / (maxIntensity - minIntensity) * SPECTROGRAM_COLOR_RANGE).toFloat()
                val color = Color.valueOf(normalizedIntensity, normalizedIntensity, normalizedIntensity)
                bitmap[x, height - y - 1] = color.toArgb()
            }
        }

        return bitmap
    }

    /**
     * Convert a Bitmap image to a ByteBuffer including scaling.
     * @param image The Bitmap image to convert.
     * @param shape The target shape for the model.
     * @return A ByteBuffer containing the image data.
     */
    fun scaledByteBufferFromBitmap(image: Bitmap, shape: ModelShape): ByteBuffer {
        val scaledBitmap = image.scale(shape.width, shape.height)
        val tensor = TensorImage(DataType.FLOAT32)
        tensor.load(scaledBitmap)
        return tensor.buffer
    }

    /**
     * Save the Bitmap image to external storage.
     * @param image The Bitmap image to save.
     * @param path The path where the image will be saved.
     * @return The file path of the saved image.
     */
    fun saveSpectrogramImage(image: Bitmap, path: String) {
        val environment = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val dir = File(environment, path)
        if(!dir.exists()) dir.mkdirs()
        val filename = "spectrogram_${image.hashCode()}.jpg"
        val inputFile = File(dir, filename)
        val fos = FileOutputStream(inputFile)
        image.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
    }
}