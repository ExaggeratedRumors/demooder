package com.ertools.demooder.core.audio

import android.app.Service
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.ertools.demooder.core.notifications.PlayerService
import com.ertools.processing.data.WavFile

/**
 * Class for playing audio using the Android MediaPlayer API.
 */
class AudioPlayer(
    private val context: Context,
    private val recordingFile: RecordingFile
): AudioProvider {
    private var mediaPlayer: MediaPlayer? = null
    private var wavFile: WavFile? = null

    override fun start() {
        if(mediaPlayer == null) {
            try {
                context.contentResolver.openInputStream(recordingFile.uri)?.use { input ->
                    wavFile = WavFile.fromStream(recordingFile.name, input)
                }
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(context, recordingFile.uri)
                    prepare()
                    start()
                    setOnCompletionListener {
                        stop()
                    }
                }
                Log.d("AudioPlayer", "Load wav file: ${wavFile?.fileName}, header: ${wavFile?.header}")
            } catch (e: Exception) {
                Log.e("AudioPlayer", "Error initializing MediaPlayer: ${e.message}")
            }
        }
        else if(mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    override fun stop() {
        if(mediaPlayer?.isPlaying == false) return
        mediaPlayer?.pause()
    }

    override fun read(buffer: ByteArray) {
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
    }

    override fun getSampleRate(): Int {
        if(mediaPlayer == null || wavFile == null) throw IllegalStateException("MediaPlayer is not initialized.")
        return wavFile!!.header.sampleRate
    }

    override fun getServiceClass(): Class<out Service> = PlayerService::class.java
}