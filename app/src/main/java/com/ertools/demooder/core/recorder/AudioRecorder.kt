package com.ertools.demooder.core.recorder

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.ertools.demooder.utils.DEBUG_MODE
import com.ertools.demooder.utils.RECORDER_DELAY_MILLIS
import com.ertools.demooder.utils.SETTINGS_DEFAULT_SIGNAL_DETECTION_PERIOD
import com.ertools.demooder.utils.isPermissionsGained
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.signal.SignalPreprocessor
import kotlin.concurrent.thread
import kotlin.math.min

class AudioRecorder (
    private val context: Context,
    private val recordingDelayMillis: Long = RECORDER_DELAY_MILLIS,
    private val recordingPeriodSeconds: Double = SETTINGS_DEFAULT_SIGNAL_DETECTION_PERIOD
) : SpectrumProvider, SoundDataProvider {
    private val bufferSize = AudioRecord.getMinBufferSize(
        ProcessingUtils.AUDIO_SAMPLING_RATE,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    ).let {
        var power = 1
        while (power * 2 <= it) { power *= 2 }
        power
    }
    private val currentData = ByteArray(bufferSize)
    private val audioBuffer = ByteArray(recordingPeriodSeconds.toInt() * bufferSize)
    private var spectrum = DoubleArray(ProcessingUtils.AUDIO_OCTAVES_AMOUNT)
    private var recorder: AudioRecord? = null
    @Volatile
    private var isRecording = false

    fun startRecording() {
        if(isRecording) return
        if(DEBUG_MODE) Log.i("SYSTEM", "Start recording.")
        initRecorder()
        if(recorder == null) return
        recorder?.startRecording()
        isRecording = true
        readDataRoutine()
    }

    fun stopRecording() {
        if(DEBUG_MODE) Log.i("SYSTEM", "Stop recording.")
        if(recorder == null) return
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
            val shiftSize = min(
                recordingPeriodSeconds * bufferSize,
                bufferSize * recordingDelayMillis / 1000.0
            ).toInt()

            while (isRecording) {
                val readSize = recorder?.read(currentData, 0, bufferSize)
                if (readSize != null && readSize != AudioRecord.ERROR_INVALID_OPERATION) {
                    spectrum = toOctaves(currentData.copyOfRange(0, readSize))
                    shiftAudioBuffer(shiftSize)
                    audioBuffer.apply { System.arraycopy(currentData, 0, this, size - shiftSize, shiftSize) }
                }
                Thread.sleep(recordingDelayMillis)
            }
        }
    }

    private fun shiftAudioBuffer(shift: Int) {
        if(shift > audioBuffer.size) return
        for (i in 0 until audioBuffer.size - shift) {
            audioBuffer[i] = audioBuffer[i + shift]
        }
    }

    private fun toOctaves(data: ByteArray) = SignalPreprocessor.run {
        data.convertToComplex()
            .fft()
            .convertSpectrumToOctavesAmplitude()
    }

    /** Spectrum provider **/
    override fun getAmplitudeSpectrum() = spectrum
    override fun getMaxAmplitude() = SignalPreprocessor.run { currentData.maxAmplitude() }

    /** Sound data provider **/
    override fun getData(): ByteArray = audioBuffer
    override fun getPeriodSeconds(): Double = recordingPeriodSeconds
}