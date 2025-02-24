package com.ertools.demooder.core.classifier

import android.content.Context
import android.util.Log
import com.ertools.demooder.utils.AppConstants
import com.ertools.processing.ModelShape
import com.ertools.processing.commons.RawData
import com.ertools.processing.data.LabelsExtraction
import org.jetbrains.kotlinx.dl.impl.inference.imagerecognition.predictTopNLabels
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxInferenceModel
import org.jetbrains.kotlinx.dl.onnx.inference.executionproviders.ExecutionProvider.CPU
import org.jetbrains.kotlinx.dl.onnx.inference.inferUsing

class ClassifierManager {
    private val configuration = ClassifierConfiguration(
        modelName = AppConstants.MODEL_NAME,
        frameSize = AppConstants.MODEL_PREPROCESSING_FRAME_SIZE,
        frameStep = AppConstants.MODEL_PREPROCESSING_FRAME_STEP,
        windowing = AppConstants.MODEL_PREPROCESSING_WINDOWING
    )
    private var model: OnnxInferenceModel? = null
    private var preprocessing: ClassifierPreprocessor? = null
    private val labels = LabelsExtraction.Emotion.entries.associate { it.ordinal to it.name }

    fun loadClassifier(context: Context) {
        model = ClassifierIO.loadOnnxModel(context, configuration)
        val shape = ModelShape.fromShapeArray(model!!.inputDimensions)
        preprocessing = ClassifierPreprocessor(shape, configuration)

        Log.i(
            "ClassifierManager",
            "Model loaded with shape: [width=${shape.width}, height=${shape.height}, channels=${shape.channels}]"
        )
    }

    fun predict(rawData: RawData, callback: (List<Pair<String, Float>>) -> (Unit)) {
        if(model == null || preprocessing == null)
            throw IllegalStateException("ClassifierManager: Model not loaded")
        Log.d("ClassifierManager", "Data size: ${rawData.size}")

        val result = model!!.inferUsing(CPU()) {
            val (data, _) = preprocessing!!.proceed(rawData)
            it.predictTopNLabels(
                floatArray = data,
                labels = labels,
                n = 2
            )
        }
        Log.d("ClassifierManager", "Prediction result: $result")

        callback(result)
    }
}