package com.ertools.demooder.core.audio

import kotlinx.coroutines.flow.StateFlow

interface AudioProvider: AudioRunningProvider {
    fun start()
    fun stop()
    fun read(buffer: ByteArray): Int?
    fun getSampleRate(): Int?
    override fun isRunning(): StateFlow<Boolean>
}