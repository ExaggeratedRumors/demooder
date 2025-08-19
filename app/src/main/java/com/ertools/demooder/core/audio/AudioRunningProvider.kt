package com.ertools.demooder.core.audio

import kotlinx.coroutines.flow.StateFlow

interface AudioRunningProvider {
    fun isRunning(): StateFlow<Boolean>
}