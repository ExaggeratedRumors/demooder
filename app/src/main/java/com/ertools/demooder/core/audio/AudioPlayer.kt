package com.ertools.demooder.core.audio

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.ertools.processing.data.WavFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.max

/**
 * Class for playing audio using the Android MediaPlayer API.
 */
class AudioPlayer(
    private val context: Context,
    private val recordingFile: RecordingFile,
    private val onStopCallback: () -> Unit = {}
): AudioProvider, ProgressProvider {
    private var mediaPlayer: MediaPlayer? = null
    private var wavFile: WavFile? = null
    private var isRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun isInitialized(): Boolean {
        return mediaPlayer != null && wavFile != null
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
                        onStopCallback()
                    }
                }
                isRunning.value = true
                Log.d("AudioPlayer", "MediaPlayer initialized and started with wav file: ${wavFile?.fileName}, header: ${wavFile?.header}")
            } catch (e: Exception) {
                Log.e("AudioPlayer", "Error initializing MediaPlayer: ${e.message}")
                e.printStackTrace()
            }
        }
        else if(mediaPlayer?.isPlaying == false) {
            try {
                mediaPlayer?.start()
                isRunning.value = true
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
        if(!isInitialized()) {
            return
        }
        try {
            isRunning.value = false
            mediaPlayer?.pause()
            Log.d("AudioPlayer", "MediaPlayer paused.")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error stopping MediaPlayer: ${e.message}")
            e.printStackTrace()
        }
    }

    override fun isRunning(): StateFlow<Boolean> = isRunning

    /**
     * Read audio data into the provided buffer.
     * The buffer size should be a multiple of the sample size.
     * @param buffer The buffer to read the audio data into.
     * @return The number of bytes read into the buffer, or -1 if the end of the audio file is reached.
     */
    override fun read(buffer: ByteArray): Int? {
        if(!isInitialized()) {
            Log.d("AudioPlayer", "Read called but MediaPlayer is not initialized.")
            return null
        }
        if(mediaPlayer!!.currentPosition == mediaPlayer!!.duration) {
            return null
        }
        val currentMillis = mediaPlayer!!.currentPosition
        val bytesPerSample = 2 * wavFile!!.header.numChannels
        val endPosition = bytesPerSample * currentMillis * wavFile!!.header.sampleRate / 1000
        val startPosition = max(0, endPosition - buffer.size)

        Log.d("AudioPlayer", "Reading audio data from position $startPosition to $endPosition (current millis: $currentMillis), buffer size: ${buffer.size}, bytes per sample: $bytesPerSample")
        if(startPosition == endPosition) return 0
        wavFile!!.data.copyInto(
            destination = buffer,
            startIndex = startPosition,
            endIndex = endPosition
        )
        return endPosition - startPosition
    }

    /**
     * Get the sample rate of the audio being played.
     * @return The sample rate in Hz, or -1 if the MediaPlayer is not initialized.
     */
    override fun getSampleRate(): Int? {
        if(!isInitialized()) return null
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
        if(position < 0 || position > wavFile!!.data.size) {
            Log.e("AudioPlayer", "Seek position out of bounds: $position")
            return
        }
        mediaPlayer?.seekTo(position)
    }
}