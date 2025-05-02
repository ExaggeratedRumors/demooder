package com.ertools.demooder.core.recorder

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.ertools.demooder.utils.AppConstants

class AudioRecorder {
    val recorderBufferSize = AudioRecord.getMinBufferSize(
        AppConstants.RECORDER_SAMPLE_RATE,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    ).let {
        var power = 1
        while (power * 2 <= it) { power *= 2 }
        power
    }

    private lateinit var recorder: AudioRecord
    private var isInitialized = false

    @SuppressLint("MissingPermission")
    fun start() {
        if(isInitialized) return
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            AppConstants.RECORDER_SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            recorderBufferSize
        )
        if(recorder.state != AudioRecord.STATE_INITIALIZED) {
            Log.e("AudioRecorder", "AudioRecord failed to initialize.")
            return
        }
        isInitialized = true
        recorder.startRecording()
        Log.i("AudioRecorder", "Recording started.")
    }

    fun stop() {
        if(!isInitialized) throw IllegalStateException("Recorder is not initialized.")
        recorder.stop()
        recorder.release()
        isInitialized = false
        Log.i("AudioRecorder", "Recording stopped.")
    }

    fun read(dataBuffer: ByteArray) {
        if(!isInitialized) throw IllegalStateException("Recorder is not initialized.")
        shiftAudioBuffer(dataBuffer, recorderBufferSize)
        val result = recorder.read(
            dataBuffer,
            dataBuffer.size - recorderBufferSize,
            recorderBufferSize
        )
        when(result) {
            AudioRecord.ERROR -> Log.w("AudioRecorder", "AudioRecord.ERROR")
            AudioRecord.ERROR_BAD_VALUE -> Log.w("AudioRecorder", "AudioRecord.ERROR_BAD_VALUE")
            AudioRecord.ERROR_INVALID_OPERATION -> Log.w("AudioRecorder", "AudioRecord.ERROR_INVALID_OPERATION")
            AudioRecord.ERROR_DEAD_OBJECT -> Log.w("AudioRecorder", "AudioRecord.ERROR_DEAD_OBJECT")
            else -> { return }
        }
    }

    private fun shiftAudioBuffer(dataBuffer: ByteArray, shift: Int) {
        if(shift > dataBuffer.size) return
        for (i in 0 until dataBuffer.size - shift) {
            dataBuffer[i] = dataBuffer[i + shift]
        }
    }
}