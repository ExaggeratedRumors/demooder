package com.ertools.demooder.core.classifier

import android.graphics.Bitmap
import androidx.compose.ui.unit.Constraints
import com.ertools.demooder.utils.DEBUG_MODE
import com.ertools.demooder.utils.Logger
import com.ertools.processing.commons.ImageDim
import com.ertools.processing.commons.LabelsExtraction
import com.ertools.processing.dataset.DatasetAndroidPreprocessor
import com.ertools.processing.io.IOManager
import org.jetbrains.kotlinx.dl.api.inference.TensorFlowInferenceModel
import org.jetbrains.kotlinx.dl.impl.inference.imagerecognition.predictTopNLabels

class ClassifierManager {
    var model: TensorFlowInferenceModel? = null
        private set

    private val labels = LabelsExtraction.Emotion.entries.map { it.ordinal to it.name }.toMap()

    fun loadClassifier(modelName: String) {
        model = IOManager.loadModel(modelName)
        if(DEBUG_MODE) Logger.log("Model $modelName loaded with shape ${model!!.shape}")
    }

    fun predict(image: Bitmap): List<Pair<String, Float>> {
        if(model == null) throw IllegalStateException("Model not loaded")



        val dim = ImageDim(model!!.shape[0], model!!.shape[1])
        val (data, shape) = DatasetAndroidPreprocessor
            .getPreprocessingPipeline(dim)
            .apply(image)

        val result = model!!.predictTopNLabels(data, labels, 2)
        return result
    }
}