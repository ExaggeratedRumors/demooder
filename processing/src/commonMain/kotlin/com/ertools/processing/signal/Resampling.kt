package com.ertools.processing.signal

import com.ertools.processing.commons.RawData

/**
 * Algorithm source: https://gist.github.com/mattmalec/6ceee1f3961ff3068727ca98ff199fab
 */
object Resampling {
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
        if(inputFrequency == outputFrequency && this.size == length) return this
        if(inputFrequency == outputFrequency) return this.copyOfRange(0, length)

        val factor = outputFrequency.toDouble() / inputFrequency.toDouble()
        val output: ByteArray
        var currentPosition = 0.0
        var outputPosition = 0
        var inputPosition = 0

        if (!isStereo) {
            /** Mono channel - 16 bits **/
            var sum = 0.0
            output = ByteArray((length * factor).toInt())

            while (outputPosition < output.size) {
                val firstValue = this[inputPosition++].toDouble()
                var nextPosition = currentPosition + factor
                if (nextPosition >= 1) {
                    sum += firstValue * (1 - currentPosition)
                    output[outputPosition++] = Math.round(sum).toByte()
                    nextPosition -= 1
                    sum = nextPosition * firstValue
                } else {
                    sum += factor * firstValue
                }
                currentPosition = nextPosition

                if (inputPosition >= length && outputPosition < output.size) {
                    output[outputPosition++] = Math.round(sum / currentPosition).toByte()
                }
            }
        } else {
            /** Stereo channel - 32 bits **/
            var sum1 = 0.0
            var sum2 = 0.0
            output = ByteArray(2 * ((length / 2) * factor).toInt())

            while (outputPosition < output.size) {
                val firstValue = this[inputPosition++].toDouble()
                val nextValue = this[inputPosition++].toDouble()
                var nextPosition = currentPosition + factor
                if (nextPosition >= 1) {
                    sum1 += firstValue * (1 - currentPosition)
                    sum2 += nextValue * (1 - currentPosition)
                    output[outputPosition++] = Math.round(sum1).toByte()
                    output[outputPosition++] = Math.round(sum2).toByte()
                    nextPosition -= 1
                    sum1 = nextPosition * firstValue
                    sum2 = nextPosition * nextValue
                } else {
                    sum1 += factor * firstValue
                    sum2 += factor * nextValue
                }
                currentPosition = nextPosition

                if (inputPosition >= length && outputPosition < output.size) {
                    output[outputPosition++] = Math.round(sum1 / currentPosition).toByte()
                    output[outputPosition++] = Math.round(sum2 / currentPosition).toByte()
                }
            }
        }
        return output
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
        if(inputFrequency == outputFrequency && this.size == length) return this
        if(inputFrequency == outputFrequency) return this.copyOfRange(0, length)

        val factor = inputFrequency.toDouble() / outputFrequency.toDouble()
        val output: ByteArray
        var currentPosition = 0.0
        var inputPosition: Int
        var diff: Double

        if(!isStereo) {
            /** Mono channel - 16 bits **/
            output = ByteArray((length / factor).toInt())
            for(i in output.indices) {
                inputPosition = currentPosition.toInt()
                diff = if(inputPosition >= length - 1) {
                    inputPosition = length - 2
                    1.0
                } else currentPosition - inputPosition
                output[i] = Math.round(this[inputPosition] * (1 - diff) + this[inputPosition + 1] * diff).toByte()
                currentPosition += factor
            }
        } else {
            /** Stereo channel - 32 bits **/
            output = ByteArray(2 * (length / 2 / factor).toInt())
            for(i in 0 until output.size / 2) {
                inputPosition = currentPosition.toInt()
                var inputRealPosition = inputPosition * 2
                diff = if(inputRealPosition >= length - 3) {
                    inputRealPosition = length - 4
                    1.0
                } else currentPosition - inputRealPosition
                output[i * 2] = Math.round(this[inputRealPosition] * (1 - diff) + this[inputRealPosition + 2] * diff).toByte()
                output[i * 2 + 1] = Math.round(this[inputRealPosition + 1] * (1 - diff) + this[inputRealPosition + 3] * diff).toByte()
                currentPosition += factor
            }
        }
        return output
    }
}