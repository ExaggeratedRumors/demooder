package com.ertools.demooder.core.recorder

interface SoundDataProvider {
    fun getData (): ByteArray
    fun getPeriodSeconds(): Double
}