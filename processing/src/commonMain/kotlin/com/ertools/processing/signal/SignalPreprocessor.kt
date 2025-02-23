package com.ertools.processing.signal

import com.ertools.processing.commons.AmplitudeSpectrum
import com.ertools.processing.commons.ComplexData
import com.ertools.processing.commons.DecibelsSpectrum
import com.ertools.processing.commons.OctavesAmplitudeSpectrum
import com.ertools.processing.data.LabelsExtraction
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.commons.RawData
import com.ertools.processing.commons.Spectrogram
import com.ertools.processing.commons.Spectrum
import com.ertools.processing.commons.ThirdsAmplitudeSpectrum
import com.ertools.processing.io.WavFile
import com.ertools.processing.spectrogram.SpectrogramSample
import org.jetbrains.kotlinx.multik.ndarray.complex.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.experimental.or
import kotlin.math.sin

import kotlin.math.*

object SignalPreprocessor {

    fun convertWavFilesToSpectrogramSamples(
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


    /**********************************/
    /** Operations on raw sound data **/
    /**********************************/

    /**
     * Downsample raw sound data
     */
    fun RawData.downSampling(length: Int, inputIsStereo: Boolean, inFrequency: Int, outFrequency: Int): RawData {
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

    /**
     * Short Time Fourier Transform on raw sound data
     */
    fun stft(
        data: RawData,
        frameSize: Int,
        stepSize: Int,
        window: Windowing.WindowType
    ): Spectrogram = Array(1 + (data.size - frameSize) / stepSize) {
        data.sliceArray(it * stepSize until it * stepSize + frameSize)
            .convertToComplex()
            .applyWindow(window)
            .fft()
            .cutInHalf()
    }

    /**
     * Convert raw sound data to complex numbers
     */
    fun RawData.convertToComplex(): ComplexData {
        val complexList = mutableListOf<ComplexDouble>()
        val buffer = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN)
        while (buffer.remaining() >= 2) {
            val sample = buffer.short.toDouble()
            complexList.add(ComplexDouble(sample, 0.0))
        }
        return complexList.toComplexDoubleArray()
    }

    /**
     * Apply windowing function to complex numbers
     */
    fun ComplexData.applyWindow(window: Windowing.WindowType): ComplexData {
        return Windowing.applyWindow(this, window)
    }

    /**
     * Fast Fourier Transform on complex numbers
     */
    fun ComplexData.fft(): Spectrum {
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

    /**
     * Find maximum amplitude in raw sound data
     */
    fun RawData.maxAmplitude(): Int {
        var maxAmplitude = 0
        for (i in this.indices step 2) {
            val sample = this[i].toShort() or (this[i + 1].toInt() shl 8).toShort()
            maxAmplitude = max(maxAmplitude, sample.toInt())
        }
        return maxAmplitude
    }


    /*****************************/
    /** Operations on amplitude **/
    /*****************************/
    fun AmplitudeSpectrum.applyWeighting(weighting: Weighting.WeightingType): AmplitudeSpectrum {
        return Weighting.applyWeighting(this, weighting)
    }

    /**
     * Save half of symmetric spectrum
     */
    fun Spectrum.cutInHalf(): Spectrum = this.sliceArray(0 until this.size / 2)

    /**
     * Convert amplitude to decibels
     */
    fun AmplitudeSpectrum.toDecibels(): DecibelsSpectrum = this.map {
        10 * log10(max(0.5, it))
    }.toDoubleArray()

    /**
     * Convert amplitude to decibels
     */
    fun Spectrum.toDecibels(): DecibelsSpectrum = this.map {
        10 * log10(max(0.5, it.re.pow(2) + it.im.pow(2)))
    }.toDoubleArray()

    /**
     * Convert STFT to amplitude spectrum
     */
    fun Spectrogram.convertStftToAmplitude(): Array<AmplitudeSpectrum> =
        Array(this.size) { frameIndex ->
            DoubleArray(this[frameIndex].size) { freqIndex ->
                val real = this[frameIndex][freqIndex].re
                val imaginary = this[frameIndex][freqIndex].im
                val result = 10 * log10(hypot(real, imaginary) + 1e-10)
                result
            }
        }

    /**
     * Convert spectrum to amplitude
     */
    fun Spectrum.convertSpectrumToAmplitude(): AmplitudeSpectrum = DoubleArray(this.size) { freqIndex ->
        val real = this[freqIndex].re
        val imaginary = this[freqIndex].im
        val result = 10 * log10(hypot(real, imaginary) + 1e-10)
        result
    }

    /**
     * Convert spectrum to octaves amplitude spectrum
     */
    fun Spectrum.convertSpectrumToOctavesAmplitude(): OctavesAmplitudeSpectrum {
        val thirdsFrequencies = (1..ProcessingUtils.AUDIO_THIRDS_AMOUNT).map {
            FrequencyOperation.cutoffFrequency(it)
        }
        val octavesFrequencies = (0 until ProcessingUtils.AUDIO_OCTAVES_AMOUNT).map {
            thirdsFrequencies[it * 3 + 2]
        }

        val freqWindow = ProcessingUtils.AUDIO_SAMPLING_RATE.toFloat() / ProcessingUtils.AUDIO_FFT_SIZE
        val octaves = DoubleArray(ProcessingUtils.AUDIO_OCTAVES_AMOUNT) { 0.0 }
        var index = 0
        var octave = 0
        for(sample in this) {
            val freq = index * freqWindow
            if(freq > octavesFrequencies[ProcessingUtils.AUDIO_OCTAVES_AMOUNT - 1]) break
            if(freq > octavesFrequencies[octave]) octave += 1
            val amplitude = sample.re.pow(2) + sample.im.pow(2)
            if(amplitude > octaves[octave]) octaves[octave] = amplitude
            index += 1
        }
        return octaves
    }

    /**
     * Convert spectrum to thirds amplitude spectrum
     */
    fun Spectrum.convectSpectrumToThirdsAmplitude(): ThirdsAmplitudeSpectrum {
        val amplitudeData = DoubleArray(ProcessingUtils.AUDIO_THIRDS_AMOUNT) { 0.0 }
        val cutoffFreq33 = FrequencyOperation.cutoffFrequency(ProcessingUtils.AUDIO_THIRDS_AMOUNT)
        val freqWindow = ProcessingUtils.AUDIO_SAMPLING_RATE.toFloat() / ProcessingUtils.AUDIO_FFT_SIZE

        var terce = 1
        var iterator = 0
        var accumulated = ComplexDouble(0, 0)
        while (iterator < this.size) {
            if((iterator * freqWindow) > cutoffFreq33 || terce > ProcessingUtils.AUDIO_THIRDS_AMOUNT) break
            accumulated += this[iterator]
            if((iterator + 1) * freqWindow > FrequencyOperation.cutoffFrequency(terce) &&
                iterator * freqWindow < ProcessingUtils.AUDIO_SAMPLING_RATE / 2f) {
                terce += 1
                continue
            }
            val absDoubledValue = accumulated.re.pow(2) + accumulated.im.pow(2)
            if(absDoubledValue > amplitudeData[terce - 1])
                amplitudeData[terce - 1] = absDoubledValue
            accumulated = ComplexDouble(0, 0)
            iterator += 1
        }
        return amplitudeData
    }

}