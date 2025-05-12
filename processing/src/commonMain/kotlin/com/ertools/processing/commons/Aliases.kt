package com.ertools.processing.commons

import org.jetbrains.kotlinx.multik.ndarray.complex.ComplexDoubleArray

typealias RawData = ByteArray
typealias ComplexData = ComplexDoubleArray
typealias Spectrum = ComplexDoubleArray
typealias AmplitudeSpectrum = DoubleArray
typealias DecibelsSpectrum = DoubleArray
typealias OctavesAmplitudeSpectrum = DoubleArray
typealias Spectrogram = Array<ComplexDoubleArray>