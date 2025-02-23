package com.ertools.processing.signal


import com.ertools.processing.commons.AmplitudeSpectrum
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sqrt

object Weighting {
    enum class WeightingType {
        A_WEIGHTING, C_WEIGHTING
    }

    fun applyWeighting(data: AmplitudeSpectrum, weighting: WeightingType): AmplitudeSpectrum {
        return data.mapIndexed { i, value ->
            val thirdIndex = i * 3
            val frequency =
                FrequencyOperation.middleFrequency(thirdIndex)
            value + applyWeighting(frequency, weighting)
        }.toDoubleArray()
    }

    fun applyWeighting(f: Double, weighting: WeightingType): Double {
        when (weighting) {
            WeightingType.A_WEIGHTING -> {
                val ra = (12200f.pow(2) * f.pow(4)) / (
                        (f.pow(2) + 20.6.pow(2)) *
                                (f.pow(2) + 12200f.pow(2)) *
                                sqrt(f.pow(2) + 107.7.pow(2)) *
                                sqrt(f.pow(2) + 737.9.pow(2))
                        )
                return 20 * log10(ra) + 2
            }
            WeightingType.C_WEIGHTING -> {
                val rc = (12200f.pow(2) * f.pow(2)) / (
                        (f.pow(2) + 20.6.pow(2)) *
                                (f.pow(2) + 12200f.pow(2))
                        )
                return 20 * log10(rc) + 0.06
            }
        }
    }
}