package com.ertools.demooder.core.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.ertools.demooder.utils.AppConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Class for recording audio using the Android AudioRecord API.
 */
class AudioRecorder: AudioProvider {
    private val sampleRate = AppConstants.RECORDER_SAMPLE_RATE
    private val recorderBufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    ).let {
        var power = 1
        while (power * 2 <= it) { power *= 2 }
        power
    }

    private lateinit var recorder: AudioRecord
    private val isPlaying = MutableStateFlow(false)

    /******************/
    /* Implementation */
    /******************/

    /**
     * Initialize the AudioRecord instance and start recording.
     */
    @SuppressLint("MissingPermission")
    override fun start() {
        if(isPlaying.value) {
            Log.d("AudioRecorder", "Start called but recorder is playing.")
            return
        }
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            recorderBufferSize
        )
        if(recorder.state != AudioRecord.STATE_INITIALIZED) {
            Log.e("AudioRecorder", "AudioRecord failed to initialize.")
            return
        }
        isPlaying.value = true
        recorder.startRecording()
        Log.d("AudioRecorder", "Recording started.")
    }

    /**
     * Stop the recording and release the AudioRecord instance.
     */
    override fun stop() {
        if(!isPlaying.value) {
            Log.d("AudioRecorder", "Stop called but recorder is not playing.")
            return
        }
        recorder.stop()
        recorder.release()
        isPlaying.value = false
        Log.d("AudioRecorder", "Recording stopped.")
    }

    /**
     * Check if the recorder is currently running.
     * @return A StateFlow indicating whether the recorder is running.
     */
    override fun isRunning(): StateFlow<Boolean> = isPlaying

    /**
     * Read audio data from the recorder into the provided buffer.
     * @param buffer The buffer to read the audio data into.
     */
    override fun read(buffer: ByteArray): Int? {
        if(!isPlaying.value) {
            Log.d("AudioRecorder", "Recorder is not playing.")
            return null
        }
        shiftAudioBuffer(buffer, recorderBufferSize)
        val result = recorder.read(
            buffer,
            buffer.size - recorderBufferSize,
            recorderBufferSize
        )
        when(result) {
            AudioRecord.ERROR -> Log.e("AudioRecorder", "AudioRecord.ERROR")
            AudioRecord.ERROR_BAD_VALUE -> Log.e("AudioRecorder", "AudioRecord.ERROR_BAD_VALUE")
            AudioRecord.ERROR_INVALID_OPERATION -> Log.e("AudioRecorder", "AudioRecord.ERROR_INVALID_OPERATION")
            AudioRecord.ERROR_DEAD_OBJECT -> Log.e("AudioRecorder", "AudioRecord.ERROR_DEAD_OBJECT")
        }
        return result
    }

    /**
     * Get the sample rate of the audio recorder.
     */
    override fun getSampleRate(): Int? = sampleRate

    /*************/
    /** Private **/
    /*************/

    private fun shiftAudioBuffer(dataBuffer: ByteArray, shift: Int) {
        if(shift > dataBuffer.size) return
        for (i in 0 until dataBuffer.size - shift) {
            dataBuffer[i] = dataBuffer[i + shift]
        }
    }
}