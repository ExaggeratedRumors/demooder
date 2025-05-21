package com.ertools.demooder.core.audio

interface AudioProvider {
    fun start()
    fun stop()
    fun read(buffer: ByteArray)
    fun getSampleRate(): Int
}