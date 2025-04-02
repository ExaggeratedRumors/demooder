package com.ertools.processing.io


import com.ertools.processing.commons.Emotion
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.commons.ProcessingUtils.SPECTROGRAM_COLOR_RANGE
import com.ertools.processing.commons.ProjectPathing
import com.ertools.processing.data.DataSource
import com.ertools.processing.signal.SignalPreprocessor
import com.ertools.processing.signal.SignalPreprocessor.convertStftToAmplitude
import com.ertools.processing.signal.Windowing
import com.ertools.processing.spectrogram.SpectrogramSample
import com.ertools.processing.spectrogram.SpectrogramsMetadata
import com.fasterxml.jackson.databind.ObjectMapper
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

object IOManager {
    private val objectMapper = ObjectMapper().apply { findAndRegisterModules() }

    /***************/
    /** Wav files **/
    /***************/
    fun fetchWavFiles(
        wavDir: String,
        maxNumberOfFiles: Int = Int.MAX_VALUE
    ): List<File> {
        val rootDir = File(ProjectPathing.DIR_AUDIO_INPUT, wavDir)
        val wavFiles = mutableListOf<File>()
        val dirs = mutableListOf(rootDir)

        rootDir.listFiles()?.forEach {
            if (it.isDirectory) dirs.add(it)
        }

        while(dirs.isNotEmpty()) {
            val dir = dirs.removeAt(0)
            println("I:\tProcessing directory ${dir.name}.")
            dir.listFiles()?.forEach {
                if (it.isDirectory) dirs.add(it)
                if (it.isFile && it.extension == ProcessingUtils.EXT_WAV_FILE) {
                    wavFiles.add(it)
                }
            }
        }

        return wavFiles.take(maxNumberOfFiles)
    }

    fun loadWavFiles(
        wavDir: String,
        maxNumberOfFiles: Int = Int.MAX_VALUE
    ): List<WavFile> = fetchWavFiles(wavDir, maxNumberOfFiles).mapNotNull {
        try {
            WavFile.fromFile(it)
        } catch (e: Exception) {
            println("E:\tSkipped file ${it.name} due to an error occurred [${e.localizedMessage}].")
            null
        }
    }

    fun saveWavFile(stream: AudioInputStream, dir: String, filename: String) {
        AudioSystem.write(
            stream,
            AudioFileFormat.Type.WAVE,
            File("${ProjectPathing.DIR_AUDIO_INPUT}/$dir", "$filename.${ProcessingUtils.EXT_WAV_FILE}")
        )
    }

    fun convertWavFilesToSpectrogramSamples(
        wavFiles: List<WavFile>,
        dataSource: DataSource,
        frameSize: Int = ProcessingUtils.SPECTROGRAM_FRAME_SIZE,
        stepSize: Int = ProcessingUtils.SPECTROGRAM_STEP_SIZE,
        window: Windowing.WindowType = Windowing.WindowType.Hamming,
        filters: (Emotion?) -> Boolean = { true }
    ): List<SpectrogramSample> = wavFiles.map { wavFile ->
        Pair(wavFile, dataSource.extractLabels(wavFile.fileName))
    }.filter { (_, labels) ->
        filters(labels)
    }.map { (file, labels) ->
        val stft = SignalPreprocessor.stft(file.data, frameSize, stepSize, window)
        SpectrogramSample(stft, file.fileName, labels)
    }


    /******************/
    /** Spectrograms **/
    /******************/
    fun saveSpectrogramMetadata(metadata: SpectrogramsMetadata, dir: String) {
        val file = File("${ProjectPathing.DIR_SPECTROGRAMS_OUTPUT}/$dir", ProcessingUtils.FILE_SPECTROGRAMS_METADATA)
        saveObject(file, metadata)
    }

    fun loadSpectrogramMetadata(dir: String): SpectrogramsMetadata {
        val file = File("${ProjectPathing.DIR_SPECTROGRAMS_OUTPUT}/$dir", ProcessingUtils.FILE_SPECTROGRAMS_METADATA)
        return loadObject(file, SpectrogramsMetadata::class.java)
    }

    fun saveSpectrogramSamples(set: List<SpectrogramSample>, dir: String) {
        set.forEach { saveSpectrogramSample(it, dir) }
    }

    fun saveSpectrogramSample(sample: SpectrogramSample, dir: String) {
        val image = complexArrayToPng(sample)
        val fileDir = File("${ProjectPathing.DIR_SPECTROGRAMS_OUTPUT}/$dir")
        if(!fileDir.exists()) fileDir.mkdir()
        val file = File(
            "${ProjectPathing.DIR_SPECTROGRAMS_OUTPUT}/$dir",
            "${sample.filename}.${ProcessingUtils.EXT_SPECTROGRAM_OUTPUT}"
        )
        ImageIO.write(image, ProcessingUtils.EXT_SPECTROGRAM_OUTPUT, file)
    }

    fun complexArrayToPng(sample: SpectrogramSample): BufferedImage {
        val magnitudeMatrix = sample.data.convertStftToAmplitude()

        /* Create image */
        val width = magnitudeMatrix.size
        val height = magnitudeMatrix[0].size

        val maxIntensity = magnitudeMatrix.maxOf { it.max() }
        val minIntensity = magnitudeMatrix.minOf { it.min() }

        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val intensity = magnitudeMatrix[x][y]
                val normalizedIntensity = ((intensity - minIntensity) / (maxIntensity - minIntensity) * SPECTROGRAM_COLOR_RANGE).toInt()
                val color = Color(normalizedIntensity, normalizedIntensity, normalizedIntensity).rgb
                image.setRGB(x, height - y - 1, color)
            }
        }
        return image
    }


    /*************/
    /** Private **/
    /*************/
    private fun <T>saveObject(file: File, data: T) = objectMapper.writeValue(file, data)

    private fun <T>loadObject(file: File, objectClass: Class<T>): T = objectMapper.readValue(file, objectClass)
}