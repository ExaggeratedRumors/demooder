package com.ertools.processing

import com.ertools.processing.augmentation.WavAugmentation.applyNoise
import com.ertools.processing.augmentation.WavAugmentation.applyPitchShift
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.io.IOManager
import org.junit.Test
import java.io.File
import javax.sound.sampled.AudioSystem

class AugmentationTest {

    @Test
    fun `apply noise test`() {
        val resourcesPath = "src/jvmTest/resources/"
        val filename = "cremad14kHz"
        val testFile = File(resourcesPath, "$filename.wav")
        val pitchFactor = 0.5f
        val noiseLevel = 0.015f

        val noisedAugmentation = AudioSystem.getAudioInputStream(testFile)
            .applyNoise(noiseLevel)
        IOManager.saveWavFile(
            noisedAugmentation,
            resourcesPath,
            "${filename}_${ProcessingUtils.WAV_AUGMENT_AFFIX}_noise"
        )

        val pitchAugmentation = AudioSystem.getAudioInputStream(testFile)
            .applyPitchShift(pitchFactor)
        IOManager.saveWavFile(
            pitchAugmentation,
            resourcesPath,
            "${filename}_${ProcessingUtils.WAV_AUGMENT_AFFIX}_pitch"
        )

    }
}