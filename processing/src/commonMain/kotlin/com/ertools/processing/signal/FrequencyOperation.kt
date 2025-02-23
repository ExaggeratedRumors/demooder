package com.ertools.processing.signal

import kotlin.math.pow

object FrequencyOperation {
    /**
     * Calculate cutoff frequency for given number of third
     */
    fun cutoffFrequency(numberOfThird: Int) =
        12.5 * 2f.pow((2f * numberOfThird - 1) / 6f)

    /**
     * Calculate middle frequency for given number of third
     */
    fun middleFrequency(numberOfThird: Int) =
        12.5 * 2f.pow((numberOfThird - 1) / 3f)
}