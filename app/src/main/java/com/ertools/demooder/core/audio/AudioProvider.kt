package com.ertools.demooder.core.audio

import android.app.Service

interface AudioProvider {
    fun start()
    fun stop()
    fun read(buffer: ByteArray)
    fun getSampleRate(): Int
}