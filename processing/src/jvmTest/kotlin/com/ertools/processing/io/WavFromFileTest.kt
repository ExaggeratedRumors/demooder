package com.ertools.processing.io

import org.junit.Test
import java.io.File

class WavFromFileTest {
    @Test
    fun `read and resample test wav file`() {
        /** Data **/
        val testDataFile = File("src/jvmTest/resources/test.wav")
        val wavFile = WavFile.fromFile(testDataFile)
        println("Filename:${wavFile.fileName}\nHeader:${wavFile.header}")
    }
}