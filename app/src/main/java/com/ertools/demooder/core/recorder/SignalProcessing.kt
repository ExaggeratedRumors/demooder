package com.ertools.demooder.core.recorder

import com.ertools.demooder.utils.AUDIO_RECORD_MAX_VALUE
import com.ertools.demooder.utils.BASIC_FREQUENCY
import com.ertools.demooder.utils.CALIBRATION
import com.ertools.demooder.utils.Complex
import com.ertools.demooder.utils.FFT_SIZE
import com.ertools.demooder.utils.SAMPLING_RATE
import com.ertools.demooder.utils.THIRDS_NUMBER
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

enum class DampingState { A_WEIGHTING, C_WEIGHTING }

fun cutoffFrequency(numberOfTerce: Int) =
BASIC_FREQUENCY * 2f.pow((2f * numberOfTerce - 1) / 6f)

fun middleFrequency(numberOfTerce: Int) =
    BASIC_FREQUENCY * 2f.pow((numberOfTerce - 1) / 3f)

fun weightingA(numberOfTerce: Int) : Int {
    val f = middleFrequency(numberOfTerce)
    val ra = (12200f.pow(2) * f.pow(4)) / (
            (f.pow(2) + 20.6.pow(2)) *
            (f.pow(2) + 12200f.pow(2)) *
                    sqrt(f.pow(2) + 107.7.pow(2)) *
                    sqrt(f.pow(2) + 737.9.pow(2))
            )
    return (20 * log10(ra) + 2).toInt()
}

fun weightingC(numberOfTerce: Int) : Int {
    val f = middleFrequency(numberOfTerce)
    val rc = (12200f.pow(2) * f.pow(2)) / (
            (f.pow(2) + 20.6.pow(2)) *
            (f.pow(2) + 12200f.pow(2))
            )
    return (20 * log10(rc) + 0.06).toInt()
}

fun convertToDecibels(amplitude: Float) =
    (10 * log10(max(0.5f, amplitude))).toInt()

fun ByteArray.convertToComplex(): Array<Complex> {
    if(this.size < FFT_SIZE * 2) throw RuntimeException("ERROR: Data size is lower than FFT_SIZE.")
    return Array(FFT_SIZE) {
        if (it > this.size) Complex()
        else Complex(
            1.0 * CALIBRATION / AUDIO_RECORD_MAX_VALUE * (
                    this[2 * it].toShort() and 0xFF or (this[2 * it + 1].toInt() shl 8).toShort()
                    )
        )
    }
}

fun Array<Complex>.fft(): Array<Complex> {
    val n = this.size
    if(n == 1) return Array(n) { this[0] }
    if(n % 2 != 0) {
        throw java.lang.RuntimeException("N is not a power of 2")
    }

    val temp = Array(n / 2) { Complex() }
    for(i in 0 until n / 2)
        temp[i] = this[2 * i]
    val q = temp.fft()

    for(i in 0 until n / 2)
        temp[i] = this[2 * i + 1]
    val r = temp.fft()

    val y = Array(n) { Complex() }
    for(i in 0 until n / 2) {
        val kth = -2 * i * PI / n
        val wk = Complex(cos(kth), sin(kth))
        y[i] = q[i].plus(wk.times(r[i]))
        y[i + n / 2] = q[i].minus(wk.times(r[i]))
    }
    return y
}

fun Array<Complex>.convertToAmplitudeOfThirds(): IntArray{
    val amplitudeData = IntArray(THIRDS_NUMBER) { 0 }
    val cutoffFreq33 = cutoffFrequency(THIRDS_NUMBER)
    val freqWindow = SAMPLING_RATE.toFloat() / FFT_SIZE
    var terce = 1
    var iterator = 0
    var accumulated = Complex()
    while (iterator < this.size) {
        if((iterator * freqWindow) > cutoffFreq33 || terce > THIRDS_NUMBER) break
        accumulated += this[iterator]
        if((iterator + 1) * freqWindow > cutoffFrequency(terce) &&
            iterator * freqWindow < SAMPLING_RATE / 2f) {
            terce += 1
            continue
        }
        val absDoubledValue = accumulated.real.pow(2) + accumulated.imag.pow(2)
        if(absDoubledValue > amplitudeData[terce - 1])
            amplitudeData[terce - 1] = absDoubledValue.toInt()
        accumulated = Complex()
        iterator += 1
    }
    return amplitudeData
}

fun getDamping(terce: Int, state: DampingState): Int {
    return when(state) {
        DampingState.A_WEIGHTING -> weightingA(terce)
        DampingState.C_WEIGHTING -> weightingC(terce)
    }
}

fun getDbSignalForm(amplitudeSpectrum: IntArray, state: DampingState): IntArray {
    val newData = IntArray(amplitudeSpectrum.size)
    for(i in amplitudeSpectrum.indices)
        newData[i] = max(
            0, getDamping(i, state) + convertToDecibels(amplitudeSpectrum[i].toFloat())
        )
    return newData
}

fun calculateMaxAmplitude(audioData: ByteArray): Int {
    var maxAmplitude = 0
    for (i in audioData.indices step 2) {
        val sample = audioData[i].toShort() or (audioData[i + 1].toInt() shl 8).toShort()
        maxAmplitude = max(maxAmplitude, sample.toInt())
    }
    return maxAmplitude
}