package com.ertools.processing

import com.ertools.processing.augmentation.WavAugmentation.applyNoise
import com.ertools.processing.augmentation.WavAugmentation.applyPitchShift
import com.ertools.processing.augmentation.WavAugmentation.applyShift
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.io.IOSoundData
import com.ertools.processing.data.WavFile
import org.junit.Test
import java.io.File
import javax.sound.sampled.AudioSystem

class AugmentationTest {

    @Test
    fun `apply noise test`() {
        val resourcesPath = "src/jvmTest/resources/"
        val filename = "cremad14kHz"
        val testFile = File(resourcesPath, "$filename.wav")
        val noiseLevel = 0.015f

        val noisedAugmentation = AudioSystem.getAudioInputStream(testFile)
            .applyNoise(noiseLevel)
        IOSoundData.saveWavFile(
            noisedAugmentation,
            "$resourcesPath/augmentation/",
            "${filename}_${ProcessingUtils.WAV_AUGMENT_AFFIX}_noise"
        )
    }

    @Test
    fun `apply pitch shift test`() {
        val resourcesPath = "src/jvmTest/resources/"
        val filename = "cremad14kHz"
        val testFile = File(resourcesPath, "$filename.wav")
        val pitchFactor = 0.5f

        val pitchAugmentation = AudioSystem.getAudioInputStream(testFile)
            .applyPitchShift(pitchFactor)
        IOSoundData.saveWavFile(
            pitchAugmentation,
            "$resourcesPath/augmentation/",
            "${filename}_${ProcessingUtils.WAV_AUGMENT_AFFIX}_pitch"
        )
    }

    @Test
    fun `apply shift test`() {
        val resourcesPath = "src/jvmTest/resources/"
        val filename = "cremad14kHz"
        val testFile = File(resourcesPath, "$filename.wav")
        val wavFile = WavFile.fromFile(testFile)
        val samplesShift = wavFile.data.size / 20

        val positiveShiftAugmentation = AudioSystem.getAudioInputStream(testFile)
            .applyShift(samplesShift)
        IOSoundData.saveWavFile(
            positiveShiftAugmentation,
            "$resourcesPath/augmentation/",
            "${filename}_${ProcessingUtils.WAV_AUGMENT_AFFIX}_shift1"
        )

        val negativeShiftAugmentation = AudioSystem.getAudioInputStream(testFile)
            .applyShift(-samplesShift)
        IOSoundData.saveWavFile(
            negativeShiftAugmentation,
            "$resourcesPath/augmentation",
            "${filename}_${ProcessingUtils.WAV_AUGMENT_AFFIX}_shift2"
        )
    }
}