package com.ertools.demooder.core.detector

import android.content.Context
import android.util.Log
import com.ertools.demooder.utils.AppConstants
import com.ertools.processing.commons.RawData
import com.ertools.processing.io.IOModel
import com.ertools.processing.model.ModelConfiguration
import com.ertools.processing.model.ModelShape
import org.tensorflow.lite.Interpreter
import kotlin.math.roundToInt

/**
 * Class for speech detection using a TensorFlow Lite model.
 */
class SpeechDetector {
    private val modelConfiguration = ModelConfiguration(
        modelName = AppConstants.SPEECH_DETECTOR_NAME,
        threadCount = AppConstants.CLASSIFIER_THREAD_COUNT,
        useNNAPI = AppConstants.CLASSIFIER_USE_NNAPI
    )

    private val detectorClassesAmount = AppConstants.DETECTOR_CLASSES_AMOUNT
    private val detectorSpeechClassId = AppConstants.DETECTOR_SPEECH_CLASS_ID
    var isModelInitialized = false

    private lateinit var shape: ModelShape
    private lateinit var detector: Interpreter
    private lateinit var preprocessor: DetectorPreprocessor

    /**
     * Load the speech detection model.
     * @param context The application context.
     */
    fun loadModel(context: Context) {
        try {
            detector = IOModel.loadModel(context, modelConfiguration)
            shape = ModelShape.fromShapeArray(detector.getInputTensor(0).shape())
            Log.d(
                "SpeechDetector",
                "Model loaded with shape: [batch=${shape.batch}, width=${shape.width}, height=${shape.height}, channels=${shape.channels}]"
            )
            preprocessor = DetectorPreprocessor(
                targetSampleRate = shape.batch,
                targetDataSize = shape.batch
            )
            isModelInitialized = true
        } catch (e: Exception) {
            Log.e("SpeechDetector", "Error loading detector: ${e.message}")
            isModelInitialized = false
        }
    }

    /**
     * Predict the speech detection result from the raw audio data.
     * @param rawData The raw audio data to process.
     * @param callback The callback to receive the prediction result.
     */
    fun detectSpeech(rawData: RawData, recordingSampleRate: Int, callback: (Boolean) -> (Unit)) {
        if (!isModelInitialized) throw IllegalStateException("Model is not initialized")
        val inputBufferList = preprocessor.proceed(
            rawData,
            sampleRate = recordingSampleRate
        )

        val predictions = inputBufferList.map { inputBuffer ->
            val outputBuffer = Array(1) { FloatArray(this.detectorClassesAmount) { Float.NaN }}
            detector.run(inputBuffer, outputBuffer)
            /* Index of max value */
            val index = outputBuffer[0].indexOfFirst { it == outputBuffer[0].max() }
            index == this.detectorSpeechClassId
        }

        Log.d("SpeechDetector", "Detection result of ${predictions.size} attempts: $predictions")
        val result = isSpeechVoting(predictions)
        callback(result)
    }

    /**
     * For each prediction, check if the speech class is the most voted.
     * @param predictions The list of predictions from the model.
     * @return True if speech is detected, false otherwise.
     */
    private fun isSpeechVoting(predictions: List<Boolean>): Boolean {
        val voting = predictions.count { it }
        return voting >= (predictions.size / 2.0).roundToInt()
    }
}