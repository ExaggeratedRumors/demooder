package com.ertools.demooder.core.classifier

import android.graphics.Bitmap
import android.os.Environment
import com.ertools.processing.model.ModelShape
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.commons.RawData
import com.ertools.processing.commons.ImageDim
import com.ertools.processing.dataset.BitmapSpectrogram
import com.ertools.processing.dataset.DatasetAndroidPreprocessor
import com.ertools.processing.signal.SignalPreprocessor
import com.ertools.processing.signal.SignalPreprocessor.downSampling
import org.jetbrains.kotlinx.dl.api.core.shape.TensorShape
import java.io.File
import java.io.FileOutputStream

class ClassifierPreprocessor(
    modelShape: ModelShape,
    private val classifierConfiguration: ClassifierConfiguration
) {
    private val pipeline = DatasetAndroidPreprocessor
        .getPreprocessingPipeline(
            ImageDim(
                modelShape.width,
                modelShape.height
            )
        )

    fun proceed(rawData: RawData): Pair<FloatArray, TensorShape> {
        val downSampled = rawData.downSampling(
            length = rawData.size,
            isStereo = false,
            inputFrequency = ProcessingUtils.AUDIO_SAMPLING_RATE,
            outputFrequency = ProcessingUtils.WAV_TARGET_SAMPLE_RATE
        )
        val stft = SignalPreprocessor.stft(
            downSampled,
            classifierConfiguration.frameSize,
            classifierConfiguration.frameStep,
            classifierConfiguration.windowing
        )
        val bitmap = BitmapSpectrogram.fromComplexSpectrogram(stft)
        saveBitmap(bitmap.image)
        val (data, shape) = pipeline.apply(bitmap.image)
        return data to shape
    }

    private fun saveBitmap(bitmap: Bitmap) {
        /** Save bitmap to external storage **/
        val environment = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val dir = File(environment, "ertools")
        if(!dir.exists()) dir.mkdirs()
        val filename = "spectrogram_${bitmap.hashCode()}.jpg"
        val inputFile = File(dir, filename)
        val fos = FileOutputStream(inputFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
    }
}