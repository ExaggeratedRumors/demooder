package com.ertools.model.augmentation

import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.io.IOManager
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import kotlin.math.roundToInt
import kotlin.random.Random

object WavAugmentation {
    fun wavFileAugmentation(
        path: String,
        numberOfFiles: Int = Integer.MAX_VALUE,
        augmentFilesAmount: Int = ProcessingUtils.WAV_AUGMENT_AMOUNT
    ): Int {
        val files = IOManager.fetchWavFiles(path, numberOfFiles)
        var counter = 0
        files.forEach { file ->
            if(file.name.contains(ProcessingUtils.WAV_AUGMENT_AFFIX)) return@forEach
            (0 until augmentFilesAmount).forEach { i ->
                val inputStream = AudioSystem.getAudioInputStream(file)

                val format = inputStream.format
                    .stretchAudio(0.85f + 0.3f * Random.nextFloat())

                val audioBytes = ByteArray(inputStream.frameLength.toInt() * format.frameSize)
                    .let { a ->
                        inputStream.read(a)
                        a
                    }
                    .addNoise(inputStream, 0.004f * Random.nextFloat())
                    .applyPitchShift(0.85f + 0.3f * Random.nextFloat())

                val outputStream = AudioInputStream(
                    audioBytes.inputStream(),
                    format,
                    audioBytes.size.toLong() / 2
                )
                IOManager.saveWavFile(
                    outputStream,
                    path,
                    "${file.nameWithoutExtension}_${ProcessingUtils.WAV_AUGMENT_AFFIX}${i + 1}"
                )
                counter += 1
            }
        }
        return counter
    }

    private fun AudioFormat.stretchAudio(speed: Float): AudioFormat {
        return AudioFormat(
            this.encoding,
            this.sampleRate * speed,
            this.sampleSizeInBits,
            this.channels,
            this.frameSize,
            this.frameRate * speed,
            this.isBigEndian
        )
    }

    private fun ByteArray.addNoise(audioInputStream: AudioInputStream, noiseLevel: Float): ByteArray {
        audioInputStream.read(this)
        for(i in this.indices) {
            val noise = ((Random.nextFloat() - 0.5f) * noiseLevel * 255).roundToInt()
            this[i] = (this[i] + noise).coerceIn(Byte.MIN_VALUE.toInt(), Byte.MAX_VALUE.toInt()).toByte()
        }
        return this
    }

    private fun ByteArray.applyPitchShift(pitchFactor: Float): ByteArray {
        val outputBuffer = mutableListOf<Byte>()

        val inputSampleCount = this.size / 2
        val outputSampleCount = (inputSampleCount * pitchFactor).toInt()

        for (i in 0 until outputSampleCount) {
            val inputSampleIndex = (i / pitchFactor).toInt()
            val sampleValue = if (inputSampleIndex < inputSampleCount) {
                val byteIndex = inputSampleIndex * 2
                (this[byteIndex].toInt() shl 8 or (this[byteIndex + 1].toInt() and 0xFF))
            } else 0
            outputBuffer.add((sampleValue shr 8).toByte())
            outputBuffer.add((sampleValue and 0xFF).toByte())
        }

        return outputBuffer.toByteArray()
    }
}