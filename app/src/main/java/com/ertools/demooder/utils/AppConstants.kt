package com.ertools.demooder.utils

import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.signal.Weighting
import com.ertools.processing.signal.Windowing


object AppConstants {
    /** App **/
    const val APP_DIR_NAME = "ertools"

    /** Classifier **/
    const val CLASSIFIER_NAME = "classifier.tflite"
    const val CLASSIFIER_PREPROCESSING_FRAME_SIZE = ProcessingUtils.SPECTROGRAM_FRAME_SIZE
    const val CLASSIFIER_PREPROCESSING_FRAME_STEP = ProcessingUtils.SPECTROGRAM_STEP_SIZE
    val CLASSIFIER_PREPROCESSING_WINDOWING = Windowing.WindowType.Hamming
    const val CLASSIFIER_THREAD_COUNT = 2
    const val CLASSIFIER_USE_NNAPI = false
    const val CLASSIFIER_RECORDING_PERIOD_MILLIS = 1000L

    /** Detector **/
    const val SPEECH_DETECTOR_NAME = "yamnet.tflite"
    const val DETECTOR_CLASSES_AMOUNT = 521
    const val DETECTOR_SPEECH_CLASS_ID = 0

    /** Recorder **/
    const val RECORDER_DELAY_MILLIS = 1L /* 250L */
    const val RECORDER_SAMPLE_RATE = ProcessingUtils.AUDIO_SAMPLING_RATE
    val RECORDER_WEIGHTING = Weighting.WeightingType.A_WEIGHTING

    /** Interface **/
    const val UI_GRAPH_UPDATE_DELAY = 100L

    /* Default settings */
    const val SETTINGS_DEFAULT_DEVICE_DAMPING = 0.0
    const val SETTINGS_DEFAULT_SIGNAL_DETECTION_PERIOD = 2.0
    const val SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS = true
    const val SETTINGS_DEFAULT_ANGER_DETECTION_TIME = 10.0
}