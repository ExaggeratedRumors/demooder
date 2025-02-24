package com.ertools.demooder.core.classifier

import com.ertools.processing.ModelShape
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.commons.RawData
import com.ertools.processing.data.ImageDim
import com.ertools.processing.dataset.BitmapSpectrogram
import com.ertools.processing.dataset.DatasetAndroidPreprocessor
import com.ertools.processing.signal.SignalPreprocessor
import com.ertools.processing.signal.SignalPreprocessor.downSampling
import org.jetbrains.kotlinx.dl.api.core.shape.TensorShape

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
            inputIsStereo = false,
            inFrequency = ProcessingUtils.AUDIO_SAMPLING_RATE,
            outFrequency = ProcessingUtils.WAV_MAX_SAMPLE_RATE
        )
        val stft = SignalPreprocessor.stft(
            downSampled,
            classifierConfiguration.frameSize,
            classifierConfiguration.frameStep,
            classifierConfiguration.windowing
        )
        val bitmap = BitmapSpectrogram.fromComplexSpectrogram(stft)

        val (data, shape) = pipeline.apply(bitmap.image)
        return data to shape
    }
}