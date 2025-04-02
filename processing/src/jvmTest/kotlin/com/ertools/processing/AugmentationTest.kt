package com.ertools.processing

import com.ertools.processing.augmentation.WavAugmentation
import com.ertools.processing.io.WavFile
import org.junit.Test
import java.io.File

class AugmentationTest {

    @Test
    fun `apply noise test`() {
        val testFile = File("src/jvmTest/resources/cremad14kHz.wav")
        val wavFile = WavFile.fromFile(testFile)
        WavAugmentation.wavFilesAugmentation()
    }
}