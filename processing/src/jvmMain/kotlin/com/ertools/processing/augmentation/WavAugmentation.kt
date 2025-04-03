package com.ertools.processing.augmentation

import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.commons.ProjectPathing
import com.ertools.processing.io.IOManager
import java.io.File
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
        val files = IOManager.fetchWavFiles(path, numberOfFiles)
        var counter = 0
        files.forEach { file ->
            if(file.name.contains(ProcessingUtils.WAV_AUGMENT_AFFIX)) return@forEach
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
            val stream = AudioSystem.getAudioInputStream(file)
                .applyNoise(0.004f * Random.nextFloat())
                .applyPitchShift(0.85f + 0.3f * Random.nextFloat())

            IOManager.saveWavFile(
                stream,
                "${ProjectPathing.DIR_AUDIO_INPUT}/$destinationPath",
                "${file.nameWithoutExtension}_${ProcessingUtils.WAV_AUGMENT_AFFIX}${i + 1}"
            )
            counter += 1
        }
        return counter
    }

    /**
     * Add noise to the audio signal.
     * @param noiseLevel Noise level to add to the audio signal. 0.0f = no noise, 1.0f = maximum noise.
     * @return AudioInputStream with noise added.
     */
    fun AudioInputStream.applyNoise(noiseLevel: Float): AudioInputStream {
        /** Read audio data bytes **/
        val audioBytes = ByteArray(this.frameLength.toInt() * this.format.frameSize).let { a ->
            this.read(a)
            a
        }
        this.read(audioBytes)

        /** Apply noise **/
        for(i in audioBytes.indices) {
            val noise = ((Random.nextFloat() - 0.5f) * noiseLevel * 255).roundToInt()
            audioBytes[i] = (audioBytes[i] + noise).coerceIn(Byte.MIN_VALUE.toInt(), Byte.MAX_VALUE.toInt()).toByte()
        }

        /** Create new audio input stream **/
        val newInputStream = AudioInputStream(
            audioBytes.inputStream(),
            this.format,
            audioBytes.size.toLong() / 2
        )
        return newInputStream
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

    fun AudioInputStream.applyShift(shift: Int): AudioInputStream {
        /** Calculate data size **/
        val audioBytes = ByteArray(this.frameLength.toInt() * this.format.frameSize).let { a ->
            this.read(a)
            a
        }
        this.read(audioBytes)

        /** Shift audio data **/
        var i = 0
        if(this.format.channels == 1) {
            val dataSize = this.frameLength.toInt() * this.format.frameSize / 2
            while(i + shift < dataSize) {
                for (j in 0 until 2) audioBytes[i + j] = audioBytes[i + j + shift]
                i += 2
            }
        } else {
            val dataSize = this.frameLength.toInt() * this.format.frameSize / 4
            while(i + shift < dataSize) {
                for (j in 0 until 4) audioBytes[i + j] = audioBytes[i + j + shift]
                i += 4
            }
        }

        /** Create new audio input stream **/
        val newInputStream = AudioInputStream(
            audioBytes.inputStream(),
            format,
            audioBytes.size.toLong() / 2
        )
        return newInputStream
    }
}