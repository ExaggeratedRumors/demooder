package com.ertools.demooder.core.detector

import android.content.Context
import android.util.Log
import com.ertools.demooder.utils.AppConstants
import com.ertools.processing.commons.RawData
import com.ertools.processing.commons.Spectrogram
import com.ertools.processing.io.IOModel
import com.ertools.processing.model.ModelConfiguration
import com.ertools.processing.signal.SignalPreprocessor
import org.tensorflow.lite.Interpreter

class SpeechDetector {
    private val configuration = ModelConfiguration(
        modelName = AppConstants.EMOTION_CLASSIFIER_NAME,
        frameSize = AppConstants.MODEL_PREPROCESSING_FRAME_SIZE,
        frameStep = AppConstants.MODEL_PREPROCESSING_FRAME_STEP,
        windowing = AppConstants.MODEL_PREPROCESSING_WINDOWING,
        threadCount = AppConstants.MODEL_THREAD_COUNT,
        useNNAPI = AppConstants.MODEL_USE_NNAPI
    )

    private lateinit var detector: Interpreter
    var isModelInitialized = false

    /**
     * Load the speech detection model.
     * @param context The application context.
     */
    fun loadModel(context: Context) {
        try {
            detector = IOModel.loadModel(context, configuration)
            isModelInitialized = true
        } catch (e: Exception) {
            Log.e("EmotionClassifier", "Error loading classifier: ${e.message}")
            isModelInitialized = false
        }
    }

    /**
     * Predict the speech detection result from the raw audio data.
     */
    fun predict(rawData: RawData, callback: (Boolean) -> (Unit)) {
        if (!isModelInitialized) throw IllegalStateException("Model is not initialized")

        val spectrogram: Spectrogram = SignalPreprocessor.stft(
            rawData,
            configuration.frameSize,
            configuration.frameStep,
            configuration.windowing
        )

        val outputBuffer = Array(1) { FloatArray(10) { Float.NaN }}
        detector.run(spectrogram, outputBuffer)
        val result = speechThreshold(outputBuffer)
        Log.d("SpeechDetector", "Prediction result: $result")
        callback(result)
    }

    private fun speechThreshold(output: Array<FloatArray>): Boolean {
        val threshold = 0.5f
        return output[0][0] > threshold
    }
}