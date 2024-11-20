package com.ertools.processing.signal

import com.ertools.processing.commons.LabelsExtraction
import com.ertools.processing.commons.Utils
import com.ertools.processing.io.WavFile
import com.ertools.processing.spectrogram.SpectrogramSample
import org.jetbrains.kotlinx.multik.ndarray.complex.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.experimental.or
import kotlin.math.sin

import kotlin.math.*

object SignalPreprocessor {
    fun processWavFile(
        wavFiles: List<WavFile>,
        frameSize: Int = 256,
        stepSize: Int = 128,
        window: Windowing.WindowType = Windowing.WindowType.Hamming,
        filters: (LabelsExtraction.Labels) -> Boolean = { true }
    ): List<SpectrogramSample> = wavFiles.map { wavFile ->
        Pair(wavFile, LabelsExtraction.fromLabel(wavFile.filename))
    }.filter { (_, labels) ->
        filters(labels)
    }.map { (file, labels) ->
        val stft = stft(file.data, frameSize, stepSize, window)
        SpectrogramSample(stft, file.filename, labels)
    }

    fun stft(
        data: ByteArray,
        frameSize: Int,
        stepSize: Int,
        window: Windowing.WindowType
    ): Array<ComplexDoubleArray> = Array(1 + (data.size - frameSize) / stepSize) {
        data.sliceArray(it * stepSize until it * stepSize + frameSize)
            .convertToComplex()
            .applyWindow(window)
            .fft()
            .cutInHalf()
    }

    fun ByteArray.convertToComplex(): ComplexDoubleArray {
        val complexList = mutableListOf<ComplexDouble>()
        val buffer = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN)
        while (buffer.remaining() >= 2) {
            val sample = buffer.short.toDouble()
            complexList.add(ComplexDouble(sample, 0.0))
        }
        return complexList.toComplexDoubleArray()
    }

    fun ComplexDoubleArray.applyWindow(window: Windowing.WindowType): ComplexDoubleArray {
        return Windowing.applyWindow(this, window)
    }

    fun DoubleArray.applyWeighting(weighting: Weighting.WeightingType): DoubleArray {
        val frameSize = this.size
        val frequencies = (0 until frameSize).map {
            it * Utils.AUDIO_SAMPLING_RATE.toFloat() / (2 * frameSize)
        }
        val weight = frequencies.map { Weighting.applyWeighting(it, weighting) }
        return this.mapIndexed { index, amplitude ->
            amplitude * weight[index]
        }.toDoubleArray()
    }

    fun ComplexDoubleArray.fft(): ComplexDoubleArray {
        val n = this.size
        if(n <= 1) return this

        val e = ComplexDoubleArray(n / 2) { this[2 * it] }.fft()
        val o = ComplexDoubleArray(n / 2) { this[2 * it + 1] }.fft()

        val result = ComplexDoubleArray(n) { ComplexDouble(0, 0) }
        for(i in 0 until n / 2) {
            val kth = -2 * i * PI / n
            val wk = ComplexDouble(cos(kth), sin(kth))
            result[i] = e[i].plus(wk.times(o[i]))
            result[i + n / 2] = e[i].minus(wk.times(o[i]))
        }
        return result
    }

    fun ComplexDoubleArray.cutInHalf(): ComplexDoubleArray = this.sliceArray(0 until this.size / 2)

    fun Array<ComplexDoubleArray>.convertStftToAmplitude(): Array<DoubleArray> =
        Array(this.size) { frameIndex ->
            DoubleArray(this[frameIndex].size) { freqIndex ->
                val real = this[frameIndex][freqIndex].re
                val imaginary = this[frameIndex][freqIndex].im
                val result = 10 * log10(hypot(real, imaginary) + 1e-10)
                result
            }
        }

    fun ComplexDoubleArray.complexToAmplitude(): DoubleArray = DoubleArray(this.size) { freqIndex ->
        val real = this[freqIndex].re
        val imaginary = this[freqIndex].im
        val result = 10 * log10(hypot(real, imaginary) + 1e-10)
        result
    }

    fun ByteArray.maxAmplitude(): Int {
        var maxAmplitude = 0
        for (i in this.indices step 2) {
            val sample = this[i].toShort() or (this[i + 1].toInt() shl 8).toShort()
            maxAmplitude = max(maxAmplitude, sample.toInt())
        }
        return maxAmplitude
    }


    fun ComplexDoubleArray.convertToAmplitudeOfThirds(): IntArray{
        val amplitudeData = IntArray(Utils.AUDIO_THIRDS_AMOUNT) { 0 }
        val cutoffFreq33 = cutoffFrequency(33)
        val freqWindow = Utils.AUDIO_SAMPLING_RATE.toFloat() / Utils.AUDIO_FFT_SIZE
        var terce = 1
        var iterator = 0
        var accumulated = ComplexDouble(0, 0)
        while (iterator < this.size) {
            if((iterator * freqWindow) > cutoffFreq33 || terce > Utils.AUDIO_THIRDS_AMOUNT) break
            accumulated += this[iterator]
            if((iterator + 1) * freqWindow > cutoffFrequency(terce) &&
                iterator * freqWindow < Utils.AUDIO_SAMPLING_RATE / 2f) {
                terce += 1
                continue
            }
            val absDoubledValue = accumulated.re.pow(2) + accumulated.im.pow(2)
            if(absDoubledValue > amplitudeData[terce - 1])
                amplitudeData[terce - 1] = absDoubledValue.toInt()
            accumulated = ComplexDouble(0, 0)
            iterator += 1
        }
        return amplitudeData
    }

    fun cutoffFrequency(numberOfTerce: Int) =
        12.5f * 2f.pow((2f * numberOfTerce - 1) / 6f)

    fun middleFrequency(numberOfTerce: Int) =
        12.5f * 2f.pow((numberOfTerce - 1) / 3f)

    fun amplitudeToDecibels(amplitudeSpectrum: DoubleArray, weighting: Weighting.WeightingType): DoubleArray {
        return amplitudeSpectrum
            .toDecibels()
            .applyWeighting(weighting)
    }

    fun DoubleArray.toDecibels(): DoubleArray = this.map {
        10 * log10(max(0.5, it))
    }.toDoubleArray()


    fun ByteArray.downsample(length: Int, inputIsStereo: Boolean, inFrequency: Int, outFrequency: Int): ByteArray {
        if(inFrequency == outFrequency && this.size == length) return this
        if(inFrequency == outFrequency) return this.copyOfRange(0, length)

        val scale = outFrequency.toDouble() / inFrequency.toDouble()
        val output: ByteArray
        var pos = 0.0
        var outPos = 0

        if (!inputIsStereo) {
            var sum = 0.0
            output = ByteArray((length * scale).toInt())
            var inPos = 0

            while (outPos < output.size) {
                val firstVal = this[inPos++].toDouble()
                var nextPos = pos + scale
                if (nextPos >= 1) {
                    sum += firstVal * (1 - pos)
                    output[outPos++] = Math.round(sum).toByte()
                    nextPos -= 1
                    sum = nextPos * firstVal
                } else {
                    sum += scale * firstVal
                }
                pos = nextPos

                if (inPos >= length && outPos < output.size) {
                    output[outPos++] = Math.round(sum / pos).toByte()
                }
            }
        } else {
            var sum1 = 0.0
            var sum2 = 0.0
            output = ByteArray(2 * ((length / 2) * scale).toInt())
            var inPos = 0

            while (outPos < output.size) {
                val firstVal = this[inPos++].toDouble()
                val nextVal = this[inPos++].toDouble()
                var nextPos = pos + scale
                if (nextPos >= 1) {
                    sum1 += firstVal * (1 - pos)
                    sum2 += nextVal * (1 - pos)
                    output[outPos++] = Math.round(sum1).toByte()
                    output[outPos++] = Math.round(sum2).toByte()
                    nextPos -= 1
                    sum1 = nextPos * firstVal
                    sum2 = nextPos * nextVal
                } else {
                    sum1 += scale * firstVal
                    sum2 += scale * nextVal
                }
                pos = nextPos

                if (inPos >= length && outPos < output.size) {
                    output[outPos++] = Math.round(sum1 / pos).toByte()
                    output[outPos++] = Math.round(sum2 / pos).toByte()
                }
            }
        }
        return output
    }
}