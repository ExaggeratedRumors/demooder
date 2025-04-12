package com.ertools.demooder.core.spectrum

import com.ertools.processing.commons.OctavesAmplitudeSpectrum
import kotlinx.coroutines.flow.StateFlow

interface SpectrumProvider {
    fun getSpectrum(): StateFlow<OctavesAmplitudeSpectrum>
}