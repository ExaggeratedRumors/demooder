package com.ertools.processing.io

import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.commons.ProjectPathing
import com.ertools.processing.spectrogram.SpectrogramImage
import com.ertools.processing.spectrogram.SpectrogramSample
import com.ertools.processing.spectrogram.SpectrogramsMetadata
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import javax.imageio.ImageIO

object IOSpectrogram {
    private val objectMapper = ObjectMapper().apply { findAndRegisterModules() }

    fun saveSpectrogramMetadata(metadata: SpectrogramsMetadata, dir: String) {
        val file = File("${ProjectPathing.DIR_SPECTROGRAMS_OUTPUT}/$dir", ProcessingUtils.FILE_SPECTROGRAMS_METADATA)
        saveObject(file, metadata)
    }

    fun loadSpectrogramMetadata(dir: String): SpectrogramsMetadata {
        val file = File("${ProjectPathing.DIR_SPECTROGRAMS_OUTPUT}/$dir", ProcessingUtils.FILE_SPECTROGRAMS_METADATA)
        return loadObject(file, SpectrogramsMetadata::class.java)
    }

    fun saveSpectrogramSamples(set: List<SpectrogramSample>, dir: String) {
        set.forEach {
            saveSpectrogramSample(it, "${ProjectPathing.DIR_SPECTROGRAMS_OUTPUT}/$dir")
        }
    }

    fun saveSpectrogramSample(sample: SpectrogramSample, dir: String) {
        val image = SpectrogramImage.fromSpectrogram(sample.data)
        val fileDir = File(dir)
        if(!fileDir.exists()) fileDir.mkdir()
        val file = File(fileDir, "${sample.filename}.${ProcessingUtils.EXT_SPECTROGRAM_OUTPUT}")
        ImageIO.write(image, ProcessingUtils.EXT_SPECTROGRAM_OUTPUT, file)
    }

    /*************/
    /** Private **/
    /*************/
    private fun <T>saveObject(file: File, data: T) = objectMapper.writeValue(file, data)

    private fun <T>loadObject(file: File, objectClass: Class<T>): T = objectMapper.readValue(file, objectClass)
}