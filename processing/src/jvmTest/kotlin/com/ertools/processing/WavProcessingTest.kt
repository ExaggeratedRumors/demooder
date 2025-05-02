package com.ertools.processing

import com.ertools.processing.data.WavFile
import org.junit.Test
import java.io.File

class WavProcessingTest {
    @Test
    fun `read and resample test wav file`() {
        /** Parameters **/
        val targetSampleRate = 16000
        val testDataFile = File("src/jvmTest/resources/cremad14kHz.wav")

        /** Read wav file **/
        val wavFile = WavFile.fromFile(testDataFile)
        println("Filename:${wavFile.fileName}\nHeader:${wavFile.header}\nSize:${wavFile.data.size}")

        /** Resample wav file **/
        wavFile.resample(targetSampleRate)

        println("Filename:${wavFile.fileName}\nHeader:${wavFile.header}\nSize:${wavFile.data.size}")
        assert(wavFile.header.sampleRate == targetSampleRate)

        /** Save new wav file **/
        val newWavFilePath = "src/jvmTest/resources/resampled"
        val resampledFile = wavFile.toFile()
        resampledFile.copyTo(File(newWavFilePath, resampledFile.name), overwrite = true)
        println("Resampled file saved to: ${File(newWavFilePath, resampledFile.name).absolutePath}")
    }
}