package com.ertools.demooder.utils

import androidx.compose.foundation.layout.WindowInsets
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.signal.Windowing

/* Debug */
const val DEBUG_MODE = true

/* Model */
const val MODEL_NAME = "model.onnx"
const val MODEL_INPUT_WIDTH = 224L
const val MODEL_INPUT_HEIGHT = 128L
const val MODEL_PREPROCESSING_FRAME_SIZE = ProcessingUtils.SPECTROGRAM_FRAME_SIZE
const val MODEL_PREPROCESSING_FRAME_STEP = ProcessingUtils.SPECTROGRAM_STEP_SIZE
val MODEL_PREPROCESSING_WINDOWING = Windowing.WindowType.Hamming

/* Recorder */
const val RECORDER_DELAY_MILLIS = 1L /* 250L */

/* Default settings */
const val SETTINGS_DEFAULT_DEVICE_DAMPING = 0.0
const val SETTINGS_DEFAULT_SIGNAL_DETECTION_PERIOD = 2.0
const val SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS = true
const val SETTINGS_DEFAULT_ANGER_DETECTION_TIME = 10.0