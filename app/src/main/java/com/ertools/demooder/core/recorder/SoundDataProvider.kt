package com.ertools.demooder.core.recorder

import com.ertools.processing.commons.RawData

interface SoundDataProvider {
    suspend fun getData (): RawData
    suspend fun getPeriodSeconds(): Double
}