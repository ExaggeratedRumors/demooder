package com.ertools.processing.signal


import com.ertools.processing.commons.ComplexData
import org.jetbrains.kotlinx.multik.ndarray.complex.mapIndexed
import org.jetbrains.kotlinx.multik.ndarray.complex.toComplexDoubleArray
import kotlin.math.PI
import kotlin.math.cos

object Windowing {
    enum class WindowType {
        Hamming, Hann, Blackman, Rectangular,
    }

    fun applyWindow(data: ComplexData, window: WindowType): ComplexData {
        when(window) {
            WindowType.Hamming ->
                return data.mapIndexed {
                        n, x -> x * (0.54 - 0.46 * cos(2 * PI * n / (data.size - 1)))
                }.toComplexDoubleArray()
            WindowType.Hann ->
                return data.mapIndexed {
                        n, x -> x * 0.5 * (1 - cos(2 * PI * n / (data.size - 1)))
                }.toComplexDoubleArray()
            WindowType.Blackman ->
                return data.mapIndexed {
                        n, x -> x * (0.42 - 0.5 * cos(2 * PI * n / (data.size - 1)) + 0.08 * cos(4 * PI * n / (data.size - 1)))
                }.toComplexDoubleArray()
            WindowType.Rectangular ->
                return data.mapIndexed {
                        _, x -> x
                }.toComplexDoubleArray()
        }
    }
}