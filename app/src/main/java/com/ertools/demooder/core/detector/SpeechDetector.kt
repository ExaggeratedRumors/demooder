package com.ertools.demooder.core.detector

import android.content.Context
import android.util.Log
import com.ertools.demooder.utils.AppConstants
import com.ertools.processing.commons.RawData
import com.ertools.processing.commons.Spectrogram
import com.ertools.processing.io.IOModel
import com.ertools.processing.model.ModelConfiguration
import com.ertools.processing.signal.SignalPreprocessor
import com.ertools.processing.spectrogram.SpectrogramConfiguration
import org.tensorflow.lite.Interpreter

class SpeechDetector {
    private val modelConfiguration = ModelConfiguration(
        modelName = AppConstants.SPEECH_DETECTOR_NAME,
        threadCount = AppConstants.MODEL_THREAD_COUNT,
        useNNAPI = AppConstants.MODEL_USE_NNAPI
    )

    private val spectrogramConfiguration = SpectrogramConfiguration(
        frameSize = AppConstants.MODEL_PREPROCESSING_FRAME_SIZE,
        frameStep = AppConstants.MODEL_PREPROCESSING_FRAME_STEP,
        windowing = AppConstants.MODEL_PREPROCESSING_WINDOWING
    )

    private lateinit var detector: Interpreter
    var isModelInitialized = false

    /**
     * Load the speech detection model.
     * @param context The application context.
     */
    fun loadModel(context: Context) {
        try {
            detector = IOModel.loadModel(context, modelConfiguration)
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
    fun detectSpeech(rawData: RawData, callback: (Boolean) -> (Unit)) {
        if (!isModelInitialized) throw IllegalStateException("Model is not initialized")

        val spectrogram: Spectrogram = SignalPreprocessor.stft(
            rawData,
            spectrogramConfiguration.frameSize,
            spectrogramConfiguration.frameStep,
            spectrogramConfiguration.windowing
        )

        val outputBuffer = Array(1) { FloatArray(10) { Float.NaN }}
        detector.run(spectrogram, outputBuffer)
        val result = speechThreshold(outputBuffer)
        Log.d("SpeechDetector", "Detection result: $result")
        callback(result)
    }

    private fun speechThreshold(output: Array<FloatArray>): Boolean {
        val threshold = 0.5f
        return output[0][0] > threshold
    }
}