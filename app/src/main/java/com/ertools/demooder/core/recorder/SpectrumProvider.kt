package com.ertools.demooder.core.recorder

interface SpectrumProvider {
    fun getAmplitudeSpectrum(): DoubleArray
    fun getMaxAmplitude(): Int
}