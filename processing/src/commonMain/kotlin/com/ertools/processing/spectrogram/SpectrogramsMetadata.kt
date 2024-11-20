package com.ertools.processing.spectrogram

import kotlin.math.max
import kotlin.math.min

data class SpectrogramsMetadata(
    var dataAmount: Long = 0L,
    var timeSizeMin: Long = Long.MAX_VALUE,
    var timeSizeMax: Long = 0L,
    var freqSizeMin: Long = Long.MAX_VALUE,
    var freqSizeMax: Long = 0L,
) {
    fun update(data: List<SpectrogramSample>) {
        try {
            this.dataAmount += data.size
            this.timeSizeMin = min(this.timeSizeMin, data.minOf { freq -> freq.data.size }.toLong())
            this.timeSizeMax = max(this.timeSizeMax, data.maxOf { freq -> freq.data.size }.toLong())
            this.freqSizeMin = min(this.freqSizeMin, data.minOf { freq ->
                if (freq.data.size == 1) freq.data[0].size
                else freq.data.minOf { time ->
                    time.size
                }
            }.toLong())
            this.freqSizeMax = max(this.freqSizeMax, data.maxOf { freq ->
                if (freq.data.size == 1) freq.data[0].size
                else freq.data.maxOf { time -> time.size }
            }.toLong())
        } catch (e: NoSuchElementException) {
            println("E:\tData is incorrect. Records are too low quality.")
        }
    }
}