package com.ertools.demooder.core.audio

interface AudioProvider {
    fun start()
    fun stop()
    fun read(buffer: ByteArray)
    fun getSampleRate(): Int
    fun getProviderType(): ProviderType

    sealed class ProviderType {
        object Microphone : ProviderType()
        object Player : ProviderType()
    }
}