package com.ertools.demooder.core.classifier

import android.content.Context
import android.util.Log
import com.ertools.demooder.utils.MODEL_INPUT_HEIGHT
import com.ertools.demooder.utils.MODEL_INPUT_WIDTH
import com.ertools.demooder.utils.MODEL_NAME
import com.ertools.demooder.utils.MODEL_PREPROCESSING_FRAME_SIZE
import com.ertools.demooder.utils.MODEL_PREPROCESSING_FRAME_STEP
import com.ertools.processing.data.LabelsExtraction
import com.ertools.processing.signal.Windowing
import org.jetbrains.kotlinx.dl.impl.inference.imagerecognition.predictTopNLabels
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxInferenceModel
import org.jetbrains.kotlinx.dl.onnx.inference.executionproviders.ExecutionProvider.CPU
import org.jetbrains.kotlinx.dl.onnx.inference.inferAndCloseUsing

class ClassifierManager {
    private val configuration = ClassifierConfiguration(
        modelName = MODEL_NAME,
        modelInputWidth = MODEL_INPUT_WIDTH,
        modelInputHeight = MODEL_INPUT_HEIGHT,
        frameSize = MODEL_PREPROCESSING_FRAME_SIZE,
        frameStep = MODEL_PREPROCESSING_FRAME_STEP,
        windowing = Windowing.WindowType.Hamming
    )
    private var model: OnnxInferenceModel? = null
    private var preprocessing: ClassifierPreprocessor? = null
    private val labels = LabelsExtraction.Emotion.entries.map { it.ordinal to it.name }.toMap()

    fun loadClassifier(context: Context) {
        model = ClassifierIO.loadOnnxModel(context, configuration)
        preprocessing = ClassifierPreprocessor(configuration)
        Log.i("ClassifierManager", "Model loaded with shape: ${model!!.inputDimensions}")
    }

    fun predict(byteData: ByteArray): List<Pair<String, Float>> {
        if(model == null || preprocessing == null)
            throw IllegalStateException("ClassifierManager: Model not loaded")

        val result = model!!.inferAndCloseUsing(CPU()) {
            val (data, shape) = preprocessing!!.processImage(byteData)
            it.reshape(shape.dims()[0], shape.dims()[1], 1L)
            it.predictTopNLabels(
                floatArray = data,
                labels = labels,
                n = 2
            )
        }

        return result
    }
}