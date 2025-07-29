package com.ertools.demooder.core.classifier

import android.content.Context
import android.util.Log
import com.ertools.demooder.utils.AppConstants
import com.ertools.processing.model.ModelShape
import com.ertools.processing.model.ModelConfiguration
import com.ertools.processing.commons.Emotion
import com.ertools.processing.commons.RawData
import com.ertools.processing.io.IOModel
import com.ertools.processing.spectrogram.SpectrogramConfiguration
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer

/**
 * Class for classifying emotions from audio data using a TensorFlow Lite model.
 */
class EmotionClassifier {
    private val modelConfiguration = ModelConfiguration(
        modelName = AppConstants.CLASSIFIER_NAME,
        threadCount = AppConstants.CLASSIFIER_THREAD_COUNT,
        useNNAPI = AppConstants.CLASSIFIER_USE_NNAPI
    )

    private val spectrogramConfiguration = SpectrogramConfiguration(
        frameSize = AppConstants.CLASSIFIER_PREPROCESSING_FRAME_SIZE,
        frameStep = AppConstants.CLASSIFIER_PREPROCESSING_FRAME_STEP,
        windowing = AppConstants.CLASSIFIER_PREPROCESSING_WINDOWING
    )

    private val labels = Emotion.entries.associateBy { it.ordinal }
    var isModelInitialized = false

    private lateinit var shape: ModelShape
    private lateinit var classifier: Interpreter
    private lateinit var preprocessor: ClassifierPreprocessor

    /**
     * Load the emotion classification model.
     * @param context The application context.
     */
    fun loadClassifier(context: Context, debugMode: Boolean = false) {
        try {
            classifier = IOModel.loadModel(context, modelConfiguration)
            shape = ModelShape.fromShapeArray(classifier.getInputTensor(0).shape())
            Log.d(
                "EmotionClassifier",
                "Model loaded with shape: [width=${shape.width}, height=${shape.height}, channels=${shape.channels}]"
            )
            preprocessor = ClassifierPreprocessor(
                spectrogramConfiguration,
                shape,
                AppConstants.CLASSIFIER_INPUT_SAMPLE_RATE,
                debugMode = debugMode
            )
            isModelInitialized = true
        } catch (e: Exception) {
            Log.e("EmotionClassifier", "Error loading classifier: ${e.message}")
            isModelInitialized = false
        }
    }

    /**
     * Predict the list of emotion voting from the raw audio data.
     * @param rawData The raw audio data to process.
     * @param recordingSampleRate The sample rate of the input audio data.
     * @param callback The callback to receive the prediction result - emotion mapped to amount of votes for this emotion.
     */
    fun predict(rawData: RawData, recordingSampleRate: Int, callback: (List<Prediction>) -> (Unit)) {
        if (!isModelInitialized) throw IllegalStateException("Model is not initialized")
        val inputBuffers: List<ByteBuffer> = preprocessor.proceed(rawData, recordingSampleRate)
        val labelsHistogram = inputBuffers.map { inputBuffer ->
            val outputBuffer = Array(1) { FloatArray(labels.size) { Float.NaN }}
            classifier.run(inputBuffer, outputBuffer)


            /*labels.map { (id, name) ->
                name to outputBuffer.last()[id]
            }.maxBy { it.second }.first*/
            labels.map { (id, name) ->
                name to outputBuffer.last()[id]
            }.toMap()
        }
            .flatMap{it.entries}
            .groupBy({it.key}, {it.value})
            .mapValues{ it.value.sum() }
            .map { Prediction(it.key, it.value) }
            .sortedByDescending { it.confidence }


        //.flatMap { it.entr}//.groupingBy { it }.eachCount()
        Log.d("EmotionClassifier", "Prediction of ${inputBuffers.size} classifications result: $labelsHistogram")

        /*val votes = labelsHistogram.entries.sumOf { it.value }
        val predictionsList = labelsHistogram.map {
            Prediction(it.key, it.value.toFloat() / votes)
        }.sortedByDescending { it.confidence }*/

        //callback(predictionsList)
        callback(labelsHistogram)
    }
}