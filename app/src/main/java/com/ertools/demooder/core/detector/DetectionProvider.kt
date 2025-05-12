package com.ertools.demooder.core.detector

import kotlinx.coroutines.flow.StateFlow

interface DetectionProvider {
    fun isSpeech(): StateFlow<Boolean>
}