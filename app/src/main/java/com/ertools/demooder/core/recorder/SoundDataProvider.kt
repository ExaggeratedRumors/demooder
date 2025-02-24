package com.ertools.demooder.core.recorder

import com.ertools.processing.commons.RawData

interface SoundDataProvider {
    fun getData (): RawData
    fun getPeriodSeconds(): Double
}