package com.ertools.demooder.core.recorder

interface SpectrumProvider {
    fun getAmplitudeSpectrum(): IntArray
    fun getMaxAmplitude(): Int
}