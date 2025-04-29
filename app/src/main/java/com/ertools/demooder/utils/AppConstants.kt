package com.ertools.demooder.utils

import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.signal.Weighting
import com.ertools.processing.signal.Windowing


object AppConstants {
    /** Model **/
    const val EMOTION_CLASSIFIER_NAME = "classifier.tflite"
    const val SPEECH_DETECTOR_NAME = "yamnet.tflite"
    const val MODEL_PREPROCESSING_FRAME_SIZE = ProcessingUtils.SPECTROGRAM_FRAME_SIZE
    const val MODEL_PREPROCESSING_FRAME_STEP = ProcessingUtils.SPECTROGRAM_STEP_SIZE
    val MODEL_PREPROCESSING_WINDOWING = Windowing.WindowType.Hamming
    const val MODEL_THREAD_COUNT = 2
    const val MODEL_USE_NNAPI = false

    /** Recorder **/
    const val RECORDER_DELAY_MILLIS = 1L /* 250L */
    val RECORDER_WEIGHTING = Weighting.WeightingType.A_WEIGHTING

    /** Interface **/
    const val UI_GRAPH_UPDATE_DELAY = 100L

    /* Default settings */
    const val SETTINGS_DEFAULT_DEVICE_DAMPING = 0.0
    const val SETTINGS_DEFAULT_SIGNAL_DETECTION_PERIOD = 2.0
    const val SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS = true
    const val SETTINGS_DEFAULT_ANGER_DETECTION_TIME = 10.0
}