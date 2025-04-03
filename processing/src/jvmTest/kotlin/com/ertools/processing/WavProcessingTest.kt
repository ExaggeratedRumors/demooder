package com.ertools.processing

import com.ertools.processing.io.WavFile
import com.ertools.processing.signal.Resampling.downSampling
import org.junit.Test
import java.io.File

class WavProcessingTest {
    @Test
    fun `read and resample test wav file`() {
        val targetSampleRate = 14000
        val testDataFile = File("src/jvmTest/resources/ravdess48kHz.wav")
        val wavFile = WavFile.fromFile(testDataFile)
        println("Filename:${wavFile.fileName}\nHeader:${wavFile.header}\nSize:${wavFile.data.size}")
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
        assert(newWavFile.header.sampleRate == targetSampleRate)
    }
}