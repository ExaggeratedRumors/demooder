package com.ertools.processing.signal

import com.ertools.processing.commons.RawData

/**
 * Algorithm source: https://gist.github.com/mattmalec/6ceee1f3961ff3068727ca98ff199fab
 */
object Resampling {

    /**
     * Resample raw sound data.
     * @param length Length of the input data.
     * @param isStereo True if the input data is stereo, false if mono.
     * @param inputFrequency Input frequency of the data.
     * @param outputFrequency Output frequency of the data.
     * @return Resampled raw sound data.
     */
    fun RawData.resample(
        length: Int,
        isStereo: Boolean,
        inputFrequency: Int,
        outputFrequency: Int
    ): RawData {
        return if (inputFrequency > outputFrequency) {
            downSampling(length, isStereo, inputFrequency, outputFrequency)
        } else {
            upSampling(length, isStereo, inputFrequency, outputFrequency)
        }
    }

    /**
     * Down-sample raw sound data.
     * @param length Length of the input data.
     * @param isStereo True if the input data is stereo, false if mono.
     * @param inputFrequency Input frequency of the data.
     * @param outputFrequency Output frequency of the data.
     * @return Down-sampled raw sound data.
     */
    fun RawData.downSampling(
        length: Int,
        isStereo: Boolean,
        inputFrequency: Int,
        outputFrequency: Int
    ): RawData {
        if (inputFrequency == outputFrequency && this.size == length) return this
        if (inputFrequency == outputFrequency) return this.copyOfRange(0, length)

        val factor = inputFrequency.toDouble() / outputFrequency.toDouble()
        val inputSampleSize = if (isStereo) 4 else 2
        val outputSamplesAmount = (length / inputSampleSize / factor).toInt()
        val output = ByteArray(outputSamplesAmount * inputSampleSize)

        var inputIndex = 0.0
        var outputIndex = 0

        for (i in 0 until outputSamplesAmount) {
            val byteIndex = inputIndex.toInt() * inputSampleSize

            if (byteIndex + inputSampleSize > this.size) break

            if (!isStereo) {
                /* Mono - 2 bytes */
                val low = this[byteIndex].toInt() and 0xFF
                val high = this[byteIndex + 1].toInt()
                val sample = ((high shl 8) or low).toShort()

                output[outputIndex++] = (sample.toInt() and 0xFF).toByte()
                output[outputIndex++] = ((sample.toInt() shr 8) and 0xFF).toByte()
            } else {
                /* Stereo - 4 bytes */
                val leftLow = this[byteIndex].toInt() and 0xFF
                val leftHigh = this[byteIndex + 1].toInt()
                val rightLow = this[byteIndex + 2].toInt() and 0xFF
                val rightHigh = this[byteIndex + 3].toInt()

                val leftSample = ((leftHigh shl 8) or leftLow).toShort()
                val rightSample = ((rightHigh shl 8) or rightLow).toShort()

                output[outputIndex++] = (leftSample.toInt() and 0xFF).toByte()
                output[outputIndex++] = ((leftSample.toInt() shr 8) and 0xFF).toByte()
                output[outputIndex++] = (rightSample.toInt() and 0xFF).toByte()
                output[outputIndex++] = ((rightSample.toInt() shr 8) and 0xFF).toByte()
            }

            inputIndex += factor
        }

        return output.copyOf(outputIndex)
    }

    /**
     * Up-sample raw sound data.
     * @param length Length of the input data.
     * @param isStereo True if the input data is stereo, false if mono.
     * @param inputFrequency Input frequency of the data.
     * @param outputFrequency Output frequency of the data.
     * @return Up-sampled raw sound data.
     */
    fun RawData.upSampling(
        length: Int,
        isStereo: Boolean,
        inputFrequency: Int,
        outputFrequency: Int
    ): RawData {
        if (inputFrequency == outputFrequency && this.size == length) return this
        if (inputFrequency == outputFrequency) return this.copyOfRange(0, length)

        val factor = outputFrequency.toDouble() / inputFrequency.toDouble()
        val inputSampleSize = if (isStereo) 4 else 2
        val outputSamplesAmount = (length / inputSampleSize * factor).toInt()
        val output = ByteArray(outputSamplesAmount * inputSampleSize)

        var inputIndex = 0.0
        var outputIndex = 0

        while (outputIndex < outputSamplesAmount) {
            val frac = inputIndex - inputIndex.toInt()

            if (inputIndex.toInt() >= length / inputSampleSize - 1) break

            val inputByteIndex = inputIndex.toInt() * inputSampleSize
            val nextByteIndex = (inputIndex.toInt() + 1) * inputSampleSize

            if (!isStereo) {
                val currentSample = ((this[inputByteIndex + 1].toInt() shl 8) or (this[inputByteIndex].toInt() and 0xFF)).toShort()
                val nextSample = ((this[nextByteIndex + 1].toInt() shl 8) or (this[nextByteIndex].toInt() and 0xFF)).toShort()

                val interpolated = (currentSample * (1 - frac) + nextSample * frac).toInt().toShort()
                output[outputIndex * 2] = (interpolated.toInt() and 0xFF).toByte()
                output[outputIndex * 2 + 1] = ((interpolated.toInt() shr 8) and 0xFF).toByte()
            } else {
                val left1 = ((this[inputByteIndex + 1].toInt() shl 8) or (this[inputByteIndex].toInt() and 0xFF)).toShort()
                val right1 = ((this[inputByteIndex + 3].toInt() shl 8) or (this[inputByteIndex + 2].toInt() and 0xFF)).toShort()

                val left2 = ((this[nextByteIndex + 1].toInt() shl 8) or (this[nextByteIndex].toInt() and 0xFF)).toShort()
                val right2 = ((this[nextByteIndex + 3].toInt() shl 8) or (this[nextByteIndex + 2].toInt() and 0xFF)).toShort()

                val leftInterpolation = (left1 * (1 - frac) + left2 * frac).toInt().toShort()
                val rightInterpolation = (right1 * (1 - frac) + right2 * frac).toInt().toShort()

                output[outputIndex * 4] = (leftInterpolation.toInt() and 0xFF).toByte()
                output[outputIndex * 4 + 1] = ((leftInterpolation.toInt() shr 8) and 0xFF).toByte()
                output[outputIndex * 4 + 2] = (rightInterpolation.toInt() and 0xFF).toByte()
                output[outputIndex * 4 + 3] = ((rightInterpolation.toInt() shr 8) and 0xFF).toByte()
            }

            outputIndex++
            inputIndex += 1.0 / factor
        }

        return output.copyOf(outputIndex * inputSampleSize)
    }
}