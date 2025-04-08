package com.ertools.demooder.core.recorder

import com.ertools.processing.commons.AmplitudeSpectrum
import com.ertools.processing.commons.OctavesAmplitudeSpectrum
import com.ertools.processing.commons.ThirdsAmplitudeSpectrum

interface SpectrumProvider {
    suspend fun getOctavesAmplitudeSpectrum(): OctavesAmplitudeSpectrum
    suspend fun getAmplitudeSpectrum(): AmplitudeSpectrum
    suspend fun getThirdsAmplitudeSpectrum(): ThirdsAmplitudeSpectrum
}