package com.ertools.demooder.core.spectrum

import com.ertools.demooder.utils.AppConstants
import com.ertools.processing.commons.RawData
import com.ertools.processing.signal.SignalPreprocessor.applyWeighting
import com.ertools.processing.signal.SignalPreprocessor.convertSpectrumToOctavesAmplitude
import com.ertools.processing.signal.SignalPreprocessor.convertToComplex
import com.ertools.processing.signal.SignalPreprocessor.fft
import com.ertools.processing.signal.SignalPreprocessor.toDecibels

object SpectrumBuilder {
    fun build(rawData: RawData) = rawData.convertToComplex()
        .fft()
        .convertSpectrumToOctavesAmplitude()
        .toDecibels()
        .applyWeighting(AppConstants.RECORDER_WEIGHTING)
}