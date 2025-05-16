package com.ertools.demooder.core.player

import android.media.MediaPlayer
import android.util.Log
import com.ertools.processing.data.WavFile
import java.io.File

/**
 * Class for playing audio using the Android MediaPlayer API.
 */
class AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var wavFile: WavFile? = null

    fun start(audioFilePath: String) {
        if(mediaPlayer == null) {
            try {
                wavFile = WavFile.fromFile(File(audioFilePath))
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(audioFilePath)
                    prepare()
                    start()
                    setOnCompletionListener {
                        stop()
                    }
                }
            } catch (e: Exception) {
                Log.e("AudioPlayer", "Error initializing MediaPlayer: ${e.message}")
            }
        }
        else if(mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    fun pause() {
        if(mediaPlayer?.isPlaying == false) return
        mediaPlayer?.pause()
    }

    fun stop() {
        if(mediaPlayer == null) return
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun getCurrentAudio(buffer: ByteArray) {
        if(mediaPlayer == null || wavFile == null) throw IllegalStateException("MediaPlayer is not initialized.")
        val currentMillis = mediaPlayer!!.currentPosition
        val bytesPerSample = 2 * wavFile!!.header.numChannels
        val endPosition = (bytesPerSample * currentMillis * wavFile!!.header.sampleRate) / 1000
        val startPosition = endPosition - buffer.size
        wavFile!!.data.copyInto(
            destination = buffer,
            startIndex = startPosition,
            endIndex = endPosition
        )
        //val startPosition = (endPosition - (bytesPerSample * periodMilliseconds) / 1000).coerceAtLeast(0)
       // return wavFile!!.data.copyOfRange(startPosition, endPosition)
    }

    fun getSampleRate(): Int {
        if(mediaPlayer == null || wavFile == null) throw IllegalStateException("MediaPlayer is not initialized.")
        return wavFile!!.header.sampleRate
    }
}