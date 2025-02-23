package com.ertools.demooder.core.classifier

import android.content.Context
import android.util.Log
import com.ertools.processing.data.ImageDim
import com.ertools.processing.data.LabelsExtraction
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.dataset.BitmapSpectrogram
import com.ertools.processing.dataset.DatasetAndroidPreprocessor
import com.ertools.processing.signal.SignalPreprocessor
import com.ertools.processing.signal.Windowing
import org.jetbrains.kotlinx.dl.api.inference.TensorFlowInferenceModel
import org.jetbrains.kotlinx.dl.impl.inference.imagerecognition.predictTopNLabels

class ClassifierManager {
    var model: TensorFlowInferenceModel? = null
        private set

    private val labels = LabelsExtraction.Emotion.entries.map { it.ordinal to it.name }.toMap()

    fun loadClassifier(context: Context, modelName: String) {

        val assetsPath = context.assets.list("")?.joinToString(", ") ?: "empty"
        Log.i("ClassifierManager", "Assets: $assetsPath")

        Log.i("ClassifierManager", "Model $modelName loaded with shape ${model!!.shape}")
    }

    fun predict(byteData: ByteArray): List<Pair<String, Float>> {
        if(model == null) throw IllegalStateException("Model not loaded")
        val spectrogram = SignalPreprocessor.stft(
            data = byteData,
            frameSize = ProcessingUtils.SPECTROGRAM_FRAME_SIZE,
            stepSize = ProcessingUtils.SPECTROGRAM_STEP_SIZE,
            window = Windowing.WindowType.Hamming
        )

        val image = BitmapSpectrogram.fromComplexSpectrogram(spectrogram).image

        val dim = ImageDim(model!!.shape[0], model!!.shape[1])
        val (data, shape) = DatasetAndroidPreprocessor
            .getPreprocessingPipeline(dim)
            .apply(image)

        val result = model!!.predictTopNLabels(data, labels, 2)
        return result
    }
}