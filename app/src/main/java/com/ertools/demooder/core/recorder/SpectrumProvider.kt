package com.ertools.demooder.core.recorder

import com.ertools.processing.commons.AmplitudeSpectrum
import com.ertools.processing.commons.OctavesAmplitudeSpectrum
import com.ertools.processing.commons.ThirdsAmplitudeSpectrum

interface SpectrumProvider {
    fun getOctavesAmplitudeSpectrum(): OctavesAmplitudeSpectrum
    fun getAmplitudeSpectrum(): AmplitudeSpectrum
    fun getThirdsAmplitudeSpectrum(): ThirdsAmplitudeSpectrum
}