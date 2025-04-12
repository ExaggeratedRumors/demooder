package com.ertools.processing

import com.ertools.processing.data.WavFile
import com.ertools.processing.signal.Resampling.downSampling
import org.junit.Test
import java.io.File

class WavProcessingTest {
    @Test
    fun `read and resample test wav file`() {
        /** Parameters **/
        val targetSampleRate = 12000
        val testDataFile = File("src/jvmTest/resources/cremad14kHz.wav")

        /** Read wav file **/
        val wavFile = WavFile.fromFile(testDataFile)
        println("Filename:${wavFile.fileName}\nHeader:${wavFile.header}\nSize:${wavFile.data.size}")

        /** Resample wav file **/
        val resampledData = wavFile.data.downSampling(
            wavFile.header.subchunk2Size,
            wavFile.header.numChannels.toInt() == 2,
            wavFile.header.sampleRate,
            targetSampleRate
        )

        /** Create new wav file **/
        val newWavFile = WavFile(
            wavFile.fileName,
            wavFile.header.copy(sampleRate = targetSampleRate),
            resampledData
        )
        println("Filename:${newWavFile.fileName}\nHeader:${newWavFile.header}\nSize:${newWavFile.data.size}")
        assert(newWavFile.header.sampleRate == targetSampleRate)

        /** Save new wav file **/
        val newWavFilePath = "src/jvmTest/resources/resampled"
        val resampledFile = newWavFile.toFile()
        resampledFile.copyTo(File(newWavFilePath, resampledFile.name), overwrite = true)
        println("Resampled file saved to: ${File(newWavFilePath, resampledFile.name).absolutePath}")
    }
}