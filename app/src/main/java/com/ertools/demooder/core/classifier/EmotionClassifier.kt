package com.ertools.demooder.core.classifier

import android.content.Context
import android.util.Log
import com.ertools.demooder.utils.AppConstants
import com.ertools.processing.model.ModelShape
import com.ertools.processing.model.ModelConfiguration
import com.ertools.processing.commons.Emotion
import com.ertools.processing.commons.RawData
import com.ertools.processing.io.IOModel
import com.ertools.processing.model.ModelPreprocessor
import com.ertools.processing.spectrogram.SpectrogramConfiguration
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer

class EmotionClassifier {
    private val modelConfiguration = ModelConfiguration(
        modelName = AppConstants.EMOTION_CLASSIFIER_NAME,
        threadCount = AppConstants.MODEL_THREAD_COUNT,
        useNNAPI = AppConstants.MODEL_USE_NNAPI
    )

    private val spectrogramConfiguration = SpectrogramConfiguration(
        frameSize = AppConstants.MODEL_PREPROCESSING_FRAME_SIZE,
        frameStep = AppConstants.MODEL_PREPROCESSING_FRAME_STEP,
        windowing = AppConstants.MODEL_PREPROCESSING_WINDOWING
    )

    private val labels = Emotion.entries.associate { it.ordinal to it.name }
    var isModelInitialized = false

    private lateinit var shape: ModelShape
    private lateinit var classifier: Interpreter
    private lateinit var preprocessor: ModelPreprocessor

    fun loadClassifier(context: Context) {
        try {
            classifier = IOModel.loadModel(context, modelConfiguration)
            shape = ModelShape.fromShapeArray(classifier.getInputTensor(0).shape())
            Log.d(
                "EmotionClassifier",
                "Model loaded with shape: [width=${shape.width}, height=${shape.height}, channels=${shape.channels}]"
            )
            preprocessor = ModelPreprocessor(spectrogramConfiguration, shape)
            isModelInitialized = true
        } catch (e: Exception) {
            Log.e("EmotionClassifier", "Error loading classifier: ${e.message}")
            isModelInitialized = false
        }
    }

    fun predict(rawData: RawData, callback: (List<Pair<String, Float>>) -> (Unit)) {
        if (!isModelInitialized) throw IllegalStateException("Model is not initialized")
        val inputBuffer: ByteBuffer = preprocessor.proceed(rawData, debug = true)
        val outputBuffer = Array(1) { FloatArray(labels.size) { Float.NaN }}
        classifier.run(inputBuffer, outputBuffer)
        val result = labels.map { (id, name) ->
            name to outputBuffer.last()[id]
        }.sortedByDescending { it.second }
        Log.d("EmotionClassifier", "Prediction result: $result")
        callback(result)
    }
}