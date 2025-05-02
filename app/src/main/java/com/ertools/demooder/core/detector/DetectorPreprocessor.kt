package com.ertools.demooder.core.detector

import com.ertools.processing.commons.RawData
import com.ertools.processing.signal.Resampling.resample
import kotlin.math.ceil
import kotlin.math.min

class DetectorPreprocessor(
    private val targetSampleRate: Int,
    private val targetDataSize: Int
) {
    /*********/
    /** API **/
    /*********/

    /**
     * Preprocess the raw data of 16-bits PCM signal to a list of FlatArray.
     * @param rawData The raw 16-bits PCM audio data to preprocess.
     */
    fun proceed(rawData: RawData, sampleRate: Int): List<FloatArray> {
        require(rawData.size % 2 == 0) { "Input data must be 16-bits or 32-bits PCM signal." }

        /* Resample to model input sample rate */
        val resampledData = rawData.resample(
            length = rawData.size,
            isStereo = false,
            inputFrequency = sampleRate,
            outputFrequency = targetSampleRate
        )

        /* Put 2-bytes data to short array */
        val shortBuffer = ShortArray(resampledData.size / 2)
        for (i in shortBuffer.indices) {
            val low = resampledData[i * 2].toInt() and 0xFF
            val high = resampledData[i * 2 + 1].toInt()
            shortBuffer[i] = ((high shl 8) or low).toShort()
        }

        /* Slice input data to groups of 1 seconds length */
        val groupsAmount = ceil(resampledData.size / 2.0 / targetDataSize).toInt()
        val detectorInput = (0 until groupsAmount).map {
            val start = it * targetDataSize
            val end = min(start + targetDataSize, shortBuffer.size)
            val slice = shortBuffer.sliceArray(start until end)
            FloatArray(targetDataSize) { i ->
                if(slice.size > i) slice[i].toFloat() / Short.MAX_VALUE
                else 0f
            }
        }
        return detectorInput
    }
}