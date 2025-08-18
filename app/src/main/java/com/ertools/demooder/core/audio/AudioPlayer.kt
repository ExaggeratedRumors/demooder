package com.ertools.demooder.core.audio

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.ertools.processing.data.WavFile
import kotlin.math.min

/**
 * Class for playing audio using the Android MediaPlayer API.
 */
class AudioPlayer(
    private val context: Context,
    private val recordingFile: RecordingFile
): AudioProvider, ProgressProvider {
    private var mediaPlayer: MediaPlayer? = null
    private var wavFile: WavFile? = null

    fun isInitialized(): Boolean {
        return mediaPlayer != null && wavFile != null
    }

    fun destroy() {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
            wavFile = null
            Log.d("AudioPlayer", "MediaPlayer and WavFile resources released.")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error releasing MediaPlayer: ${e.message}")
            e.printStackTrace()
        }
    }

    /*********************************/
    /* Audio Provider implementation */
    /*********************************/

    /**
     * Start playing the audio file.
     * If the MediaPlayer is not initialized, it will load the wav file from the recordingFile URI.
     */
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
                e.printStackTrace()
            }
        }
        else if(mediaPlayer?.isPlaying == false) {
            try {
                mediaPlayer?.start()
            } catch (e: Exception) {
                Log.e("AudioPlayer", "Error starting MediaPlayer: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    /**
     * Stop playing the audio file.
     * If the MediaPlayer is not playing, it will pause the playback.
     */
    override fun stop() {
        if(!isInitialized()) return
        try {
            mediaPlayer?.pause()
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error stopping MediaPlayer: ${e.message}")
            e.printStackTrace()
        }
    }


    /**
     * Read audio data into the provided buffer.
     * The buffer size should be a multiple of the sample size.
     * @param buffer The buffer to read the audio data into.
     */
    override fun read(buffer: ByteArray) {
        if(!isInitialized()) throw IllegalStateException("MediaPlayer is not initialized.")
        val currentMillis = mediaPlayer!!.currentPosition
        val bytesPerSample = 2 * wavFile!!.header.numChannels
        val endPosition = bytesPerSample * currentMillis * wavFile!!.header.sampleRate / 1000
        val startPosition = min(0, endPosition - buffer.size)
        if(mediaPlayer!!.currentPosition == mediaPlayer!!.duration) destroy()
        if(startPosition <= endPosition) return

        Log.d("AudioPlayer", "Reading audio data: start=$startPosition, end=$endPosition, bufferSize=${buffer.size}, currentMillis=$currentMillis numChannels=${wavFile!!.header.numChannels}, sampleRate=${wavFile!!.header.sampleRate}")
        wavFile!!.data.copyInto(
            destination = buffer,
            startIndex = startPosition,
            endIndex = endPosition
        )
    }

    /**
     * Get the sample rate of the audio being played.
     */
    override fun getSampleRate(): Int {
        if(!isInitialized()) throw IllegalStateException("MediaPlayer is not initialized.")
        return wavFile!!.header.sampleRate
    }

    /************************************/
    /* Progress Provider implementation */
    /************************************/
    override fun getSize(): Int {
        return mediaPlayer?.duration ?: 0
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    override fun seekTo(position: Int) {
        if(!isInitialized()) return
        if(mediaPlayer == null) throw IllegalStateException("MediaPlayer is not initialized.")
        if(position < 0 || position > wavFile!!.data.size) {
            throw IllegalArgumentException("Position out of bounds: $position")
        }
        mediaPlayer?.seekTo(position)
    }
}