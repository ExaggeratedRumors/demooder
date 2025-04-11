package com.ertools.processing.io


import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.commons.ProjectPathing
import com.ertools.processing.data.WavFile
import com.ertools.processing.signal.SignalPreprocessor
import com.ertools.processing.signal.Windowing
import com.ertools.processing.spectrogram.SpectrogramSample
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

object IOSoundData {
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
            File(dir, "$filename.${ProcessingUtils.EXT_WAV_FILE}")
        )
    }

    fun convertWavFilesToSpectrogramSamples(
        wavFiles: List<WavFile>,
        frameSize: Int = ProcessingUtils.SPECTROGRAM_FRAME_SIZE,
        stepSize: Int = ProcessingUtils.SPECTROGRAM_STEP_SIZE,
        window: Windowing.WindowType = Windowing.WindowType.Hamming
    ): List<SpectrogramSample> = wavFiles.map { file ->
        val stft = SignalPreprocessor.stft(file.data, frameSize, stepSize, window)
        SpectrogramSample(stft, file.fileName)
    }

    /*************/
    /** Private **/
    /*************/
    private fun <T>saveObject(file: File, data: T) = objectMapper.writeValue(file, data)

    private fun <T>loadObject(file: File, objectClass: Class<T>): T = objectMapper.readValue(file, objectClass)
}