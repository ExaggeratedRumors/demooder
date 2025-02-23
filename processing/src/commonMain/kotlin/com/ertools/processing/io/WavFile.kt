package com.ertools.processing.io

import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.signal.SignalPreprocessor.downSampling
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class WavFile(file: File) {
    var filename: String
        private set
    private var header: WavHeader
    var data: ByteArray
        private set

    init {
        FileInputStream(file).use { inputStream ->
            try {
                filename = file.nameWithoutExtension
                header = inputStream.readHeader()
                validate()
                val dataBuffer = ByteArray(header.subchunk2Size)
                println(header)
                inputStream.read(dataBuffer)
                data = if(header.subchunk2Id == "data") dataBuffer
                else dataBuffer.downSampling(ProcessingUtils.WAV_MAX_LENGTH,true, header.sampleRate, ProcessingUtils.WAV_MAX_SAMPLE_RATE)
            } catch (e: Exception) {
                println("E: ${e.localizedMessage}")
                throw e
            }
        }
    }

    /**************/
    /** Privates **/
    /**************/
    private fun FileInputStream.readHeader(): WavHeader {
        val headerBytes = ByteArray(ProcessingUtils.WAV_HEADER_SIZE)
        this.read(headerBytes)

        var cursor = 0
        val stringSize = ProcessingUtils.WAV_STRING_SIZE
        val buffer = ByteBuffer.wrap(headerBytes).order(ByteOrder.LITTLE_ENDIAN)

        val chunkId = ByteArray(stringSize).apply { buffer.get(this); cursor += this.size }.decodeToString()
        val chunkSize = buffer.int.also { cursor += Int.SIZE_BYTES }
        val format = ByteArray(stringSize).apply { buffer.get(this); cursor += this.size }.decodeToString()
        val subchunk1Id = ByteArray(stringSize).apply { buffer.get(this); cursor += this.size }.decodeToString()
        val subchunk1Size = buffer.int.also { cursor += Int.SIZE_BYTES }
        val audioFormat = buffer.short.also { cursor += Short.SIZE_BYTES }
        val numChannels = buffer.short.also { cursor += Short.SIZE_BYTES }
        val sampleRate = buffer.int.also { cursor += Int.SIZE_BYTES }
        val byteRate = buffer.int.also { cursor += Int.SIZE_BYTES }
        val blockAlign = buffer.short.also { cursor += Short.SIZE_BYTES }
        val bitsPerSample = buffer.short.also { cursor += Short.SIZE_BYTES }
        val subchunk2Id = ByteArray(stringSize).apply { buffer.get(this); cursor += this.size }.decodeToString()

        val subchunk2Size = if(subchunk2Id == "LIST") {
            val additionalHeaderBytes = ByteArray(ProcessingUtils.WAV_ADDITIONAL_HEADER_SIZE)
            this.read(additionalHeaderBytes)
            val additionalBuffer = ByteBuffer.wrap(additionalHeaderBytes).order(ByteOrder.LITTLE_ENDIAN)
            additionalBuffer.int.also { cursor += Int.SIZE_BYTES }
        } else {
            buffer.int.also { cursor += Int.SIZE_BYTES }
        }

        return WavHeader(
            chunkId, chunkSize, format, subchunk1Id, subchunk1Size,
            audioFormat, numChannels, sampleRate, byteRate, blockAlign,
            bitsPerSample, subchunk2Id, subchunk2Size
        )
    }

    private fun validate() {
        if(header.chunkId != "RIFF" || header.format != "WAVE")
            throw IllegalArgumentException("Incorrect file format.")
        if(header.audioFormat != 1.toShort())
            throw IllegalArgumentException("Only uncompressed PCM files are supported.")
        if(header.bitsPerSample != 16.toShort())
            throw IllegalArgumentException("Only 16-bits files are supported.")
    }

    /**********************/
    /** Header structure **/
    /**********************/
    data class WavHeader(
        val chunkId: String,
        val chunkSize: Int,
        val format: String,
        val subchunk1Id: String,
        val subchunk1Size: Int,
        val audioFormat: Short,
        val numChannels: Short,
        val sampleRate: Int,
        val byteRate: Int,
        val blockAlign: Short,
        val bitsPerSample: Short,
        val subchunk2Id: String,
        val subchunk2Size: Int
    )
}