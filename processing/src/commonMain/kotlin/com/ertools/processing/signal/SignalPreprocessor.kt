package com.ertools.processing.signal

import com.ertools.processing.commons.LabelsExtraction
import com.ertools.processing.commons.Utils
import com.ertools.processing.io.WavFile
import com.ertools.processing.spectrogram.SpectrogramSample
import com.ertools.processing.signal.Weighting
import com.ertools.processing.signal.Windowing
import org.jetbrains.kotlinx.multik.ndarray.complex.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.sin

import kotlin.math.*

object SignalPreprocessor {
    fun process(
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

    fun ComplexDoubleArray.applyWeighting(weighting: Weighting.WeightingType): ComplexDoubleArray {
        val frameSize = this.size
        val frequencies = (0 until frameSize).map {
            it * Utils.SPECTROGRAM_SAMPLING_RATE.toFloat() / (2 * frameSize)
        }
        val weight = frequencies.map { Weighting.applyWeighting(it, weighting) }
        return this.mapIndexed { index, amplitude ->
            amplitude * weight[index]
        }.toComplexDoubleArray()
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

    fun Array<ComplexDoubleArray>.convertStftToDb(): Array<DoubleArray> = Array(this.size) { frameIndex ->
        DoubleArray(this[frameIndex].size) { freqIndex ->
            val real = this[frameIndex][freqIndex].re
            val imaginary = this[frameIndex][freqIndex].im
            val result = 10 * log10(hypot(real, imaginary) + 1e-10)
            result
        }
    }

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