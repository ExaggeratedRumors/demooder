package com.ertools.demooder.core.recorder

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.ertools.demooder.utils.DEBUG_MODE
import com.ertools.demooder.utils.READ_DATA_DELAY
import com.ertools.demooder.utils.isPermissionsGained
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.signal.SignalPreprocessor
import kotlin.concurrent.thread
import kotlin.math.max

class AudioRecorder (private val context: Context) : SpectrumProvider {
    private val bufferSize = AudioRecord.getMinBufferSize(
        ProcessingUtils.AUDIO_SAMPLING_RATE,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    ).let {
        var power = 1
        while (power * 2 <= it) { power *= 2 }
        power
    }
    private val data = ByteArray(max(bufferSize, 0))
    private var spectrum = DoubleArray(ProcessingUtils.AUDIO_OCTAVES_AMOUNT)
    private var recorder: AudioRecord? = null
    @Volatile
    private var isRecording = false

    fun startRecording() {
        if(isRecording) return
        if(DEBUG_MODE) Log.i("SYSTEM", "Start recording.")
        initRecorder()
        recorder?.startRecording()
        isRecording = true
        readDataRoutine()
    }

    fun stopRecording() {
        if(DEBUG_MODE) Log.i("SYSTEM", "Stop recording.")
        isRecording = false
        recorder?.stop()
        recorder?.release()
        recorder = null
    }

    @SuppressLint("MissingPermission")
    private fun initRecorder() {
        recorder = if (!isPermissionsGained(context)) null
        else AudioRecord(
            MediaRecorder.AudioSource.MIC,
            ProcessingUtils.AUDIO_SAMPLING_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )
    }

    private fun readDataRoutine() {
        thread {
            while (isRecording) {
                val readSize = recorder?.read(data, 0, bufferSize)
                if (readSize != null && readSize != AudioRecord.ERROR_INVALID_OPERATION)
                    spectrum = processData(data.copyOfRange(0, readSize))
                Thread.sleep(READ_DATA_DELAY)
            }
        }
    }

    private fun processData(data: ByteArray) = SignalPreprocessor.run {
        data.convertToComplex()
            .fft()
            .convertSpectrumToOctavesAmplitude()
    }

    override fun getAmplitudeSpectrum() = spectrum
    override fun getMaxAmplitude() = SignalPreprocessor.run { data.maxAmplitude() }
}