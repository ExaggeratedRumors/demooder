package com.ertools.processing.io

import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.signal.Resampling.downSampling
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class WavFile(
    fileName: String,
    header: WavHeader,
    data: ByteArray,
) {
    var fileName: String = fileName
        private set
    var header: WavHeader = header
        private set
    var data: ByteArray = data
        private set


    companion object {
        fun fromFile(file: File): WavFile {
            FileInputStream(file).use { inputStream ->
                val filename = file.nameWithoutExtension

                /** Read header **/
                val header = try {
                    inputStream.readHeader()
                } catch (e: Exception) {
                    throw WavException("Error reading header.", e)
                }
                headerValidate(header)

                /** Read and resample data **/
                val data = ByteArray(header.subchunk2Size)
                inputStream.read(data)
                return WavFile(filename, header, data)
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

            val subchunk2Size = when (subchunk2Id) {
                "LIST" -> {
                    val additionalHeaderBytes = ByteArray(ProcessingUtils.WAV_ADDITIONAL_HEADER_SIZE)
                    this.read(additionalHeaderBytes)
                    val additionalBuffer =
                        ByteBuffer.wrap(additionalHeaderBytes).order(ByteOrder.LITTLE_ENDIAN)
                    additionalBuffer.int
                }
                "FLLR" -> {
                    val skipDataBytes = buffer.int.also { cursor += Int.SIZE_BYTES}
                    val skipDataByteArray = ByteArray(skipDataBytes)
                    this.read(skipDataByteArray)

                    val dataInfoBytes = stringSize + Int.SIZE_BYTES
                    val dataInfoByteArray = ByteArray(dataInfoBytes)
                    this.read(dataInfoByteArray)
                    val dataInfoBuffer = ByteBuffer.wrap(dataInfoByteArray).order(ByteOrder.LITTLE_ENDIAN)
                    val id = ByteArray(stringSize).apply { dataInfoBuffer.get(this); cursor += this.size }.decodeToString()
                    if(id != "data") throw WavException("Unknown subchunk2Id inside FLLR: $subchunk2Id.")
                    dataInfoBuffer.int
                }
                "data" -> {
                    buffer.int
                }
                else -> {
                    throw WavException("Unknown subchunk2Id: $subchunk2Id.")
                }
            }

            return WavHeader(
                chunkId, chunkSize, format, subchunk1Id, subchunk1Size,
                audioFormat, numChannels, sampleRate, byteRate, blockAlign,
                bitsPerSample, subchunk2Id, subchunk2Size
            )
        }

        private fun headerValidate(header: WavHeader) {
            if(header.chunkId != "RIFF" || header.format != "WAVE")
                throw WavException("Incorrect file format: ${header}.")
            if(header.audioFormat != 1.toShort())
                throw WavException("Only uncompressed PCM files are supported.")
            if(header.bitsPerSample != 16.toShort())
                throw WavException("Only 16-bits files are supported.")
        }
    }

    fun resample(targetSampleRate: Int) {
        val resampledData = data.downSampling(
            header.subchunk2Size,
            header.numChannels.toInt() == 2,
            header.sampleRate,
            targetSampleRate
        )
        this.data = resampledData
        //this.header = header.copy(sampleRate = targetSampleRate)
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

    class WavException(
        message: String,
        cause: Throwable? = null
    ): Exception(message, cause)
}