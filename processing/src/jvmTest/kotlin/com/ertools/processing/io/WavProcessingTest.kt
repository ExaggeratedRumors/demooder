package com.ertools.processing.io

import com.ertools.processing.signal.SignalPreprocessor.downSampling
import org.junit.Test
import java.io.File

class WavProcessingTest {
    @Test
    fun `read test wav file`() {
        val testDataFile = File("src/jvmTest/resources/test.wav")
        val wavFile = WavFile.fromFile(testDataFile)
        println("Filename:${wavFile.fileName}\nHeader:${wavFile.header}\nSize:${wavFile.data.size}")
    }

    @Test
    fun `resample wav file`() {
        val targetSampleRate = 14000
        val testDataFile = File("src/jvmTest/resources/test.wav")
        val wavFile = WavFile.fromFile(testDataFile)
        println("Filename:${wavFile.fileName}\nHeader:${wavFile.header}")
        val resampledData = wavFile.data.downSampling(
            wavFile.header.subchunk2Size,
            wavFile.header.numChannels.toInt() == 2,
            wavFile.header.sampleRate,
            targetSampleRate
        )
        val newWavFile = WavFile(
            wavFile.fileName,
            wavFile.header.copy(sampleRate = targetSampleRate),
            resampledData
        )
        println("Filename:${newWavFile.fileName}\nHeader:${newWavFile.header}\nSize:${newWavFile.data.size}")
    }

}