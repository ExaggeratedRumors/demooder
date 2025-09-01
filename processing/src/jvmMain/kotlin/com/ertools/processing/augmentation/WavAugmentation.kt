package com.ertools.processing.augmentation

import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.commons.ProjectPathing
import com.ertools.processing.io.IOAudioData
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import kotlin.math.roundToInt
import kotlin.random.Random

object WavAugmentation {
    /**
     * Augment all wav files in `path` directory.
     * @param path Directory path to search for wav files.
     * @param numberOfFiles Maximum number of files to process.
     * @param augmentFilesAmount Number of augmented files to create from each file.
     * @return Number of augmented files created.
     */
    fun wavFilesAugmentation(
        path: String,
        numberOfFiles: Int = Integer.MAX_VALUE,
        augmentFilesAmount: Int = ProcessingUtils.WAV_AUGMENT_AMOUNT
    ): Int {
        val files = IOAudioData.fetchWavFiles(path, numberOfFiles)
        var counter = 0
        files.forEach { file ->
            if (file.name.contains(ProcessingUtils.WAV_AUGMENT_AFFIX)) return@forEach
            counter += wavFileAugmentation(file, path, augmentFilesAmount)
        }
        return counter
    }

    /**
     * Create `augmentFilesAmount` augmented files from `file` and save them in `destinationPath`.
     * @param file File to augment.
     * @param destinationPath Path to save the augmented files.
     * @param augmentFilesAmount Number of augmented files to create.
     * @return Number of augmented files created.
     */
    fun wavFileAugmentation(file: File, destinationPath: String, augmentFilesAmount: Int): Int {
        var counter = 0
        (0 until augmentFilesAmount).forEach { i ->
            try {
                val stream = AudioSystem.getAudioInputStream(file)
                    .applyNoise(0.004f * Random.nextFloat())
                    .applyPitchShift(0.85f + 0.3f * Random.nextFloat())

                IOAudioData.saveWavFile(
                    stream,
                    "${ProjectPathing.DIR_AUDIO_INPUT}/$destinationPath",
                    "${file.nameWithoutExtension}_${ProcessingUtils.WAV_AUGMENT_AFFIX}${i + 1}"
                )
                counter += 1
            } catch (e: IllegalArgumentException) {
                println("E:\tSkipped file ${file.name} due to an error occurred [${e.localizedMessage}].")
            }
        }
        return counter
    }

    /**
     * Add noise to the audio signal.
     * @param noiseLevel Noise level to add to the audio signal. 0.0f = no noise, 1.0f = maximum noise.
     * @return AudioInputStream with noise added.
     */
    fun AudioInputStream.applyNoise(noiseLevel: Float): AudioInputStream {
        require(this.format.sampleSizeInBits == 16) {
            "Only 16-bit audio format is supported for noise augmentation."
        }
        /** Read audio data bytes **/
        val audioBytes = ByteArray(this.frameLength.toInt() * this.format.frameSize).also {
            this.read(it)
        }

        var i = 0
        while (i < audioBytes.size) {
            val l = audioBytes[i]
            val h = audioBytes[i + 1]
            val sample = ByteBuffer.wrap(byteArrayOf(l, h))
                .order(ByteOrder.LITTLE_ENDIAN)
                .short.toInt()
            val noise = ((Random.nextFloat() - 0.5f) * 2f * noiseLevel * Short.MAX_VALUE).roundToInt()
            val noisedSample = (sample + noise).coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())

            audioBytes[i] = (noisedSample and 0xFF).toByte()
            audioBytes[i + 1] = ((noisedSample shr 8) and 0xFF).toByte()

            i += 2
        }

        /** Create new audio input stream **/
        return AudioInputStream(
            ByteArrayInputStream(audioBytes),
            this.format,
            (audioBytes.size / this.format.frameSize).toLong()
        )
    }

    /**
     * Reduce signal samples without changing sampling rate. Effect is similar to speed the signal.
     * @param pitchFactor Pitch factor to shift the audio signal. 1.0f = no shift, 0.5f = half samples, 2.0f = double samples.
     * @return AudioInputStream with new format.
     */
    fun AudioInputStream.applyPitchShift(pitchFactor: Float): AudioInputStream {
        /** Check mono or stereo **/
        if (this.format.channels != 1) throw IllegalArgumentException("Only mono audio format is supported.")

        /** Read audio data bytes **/
        val audioBytes = ByteArray(this.frameLength.toInt() * this.format.frameSize).let { a ->
            this.read(a)
            a
        }
        this.read(audioBytes)

        /** Create new audio format **/
        val outputBuffer = mutableListOf<Byte>()
        val inputSampleCount = audioBytes.size / 2
        val outputSampleCount = (inputSampleCount * pitchFactor).toInt()

        /** Resample audio data **/
        for (i in 0 until outputSampleCount) {
            val inputSampleIndex = (i / pitchFactor).toInt()
            if (inputSampleIndex < inputSampleCount) {
                val byteIndex = inputSampleIndex * 2
                outputBuffer.add(audioBytes[byteIndex])
                outputBuffer.add(audioBytes[byteIndex + 1])
            } else {
                outputBuffer.add(0)
                outputBuffer.add(0)
            }
        }

        /** Create new audio input stream **/
        val newInputStream = AudioInputStream(
            outputBuffer.toByteArray().inputStream(),
            format,
            outputBuffer.size.toLong() / 2
        )
        return newInputStream
    }

    /**
     * Shift audio data by `shift` frames full samples (2 bytes for mono or 4 bytes for stereo)
     * by crop the beginning or end of the audio data.
     * @param shift Number of frames to shift the audio data. Positive value shifts to the left, negative value shifts to the right.
     * @return AudioInputStream with new format.
     */
    fun AudioInputStream.applyShift(shiftFrames: Int): AudioInputStream {
        if (shiftFrames == 0) return this

        val frameSize = this.format.frameSize
        val totalFrames = this.frameLength.toInt()

        val audioBytes = ByteArray(totalFrames * frameSize).also {
            this.read(it)
        }

        val startFrame = if (shiftFrames < 0) -shiftFrames else 0
        val endFrame = if (shiftFrames > 0) totalFrames - shiftFrames else totalFrames

        if (startFrame >= endFrame) return AudioInputStream(ByteArray(0).inputStream(), format, 0)

        /** Create new audio input stream **/
        val newFrameCount = endFrame - startFrame
        val outputBytes = audioBytes.copyOfRange(
            startFrame * frameSize,
            endFrame * frameSize
        )

        return AudioInputStream(
            outputBytes.inputStream(),
            format,
            newFrameCount.toLong()
        )
    }
}