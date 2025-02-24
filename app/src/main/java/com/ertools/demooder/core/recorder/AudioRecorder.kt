package com.ertools.demooder.core.recorder

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.ertools.demooder.utils.AppConstants
import com.ertools.demooder.utils.isPermissionsGained
import com.ertools.processing.commons.AmplitudeSpectrum
import com.ertools.processing.commons.OctavesAmplitudeSpectrum
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.commons.ThirdsAmplitudeSpectrum
import com.ertools.processing.signal.SignalPreprocessor.applyWeighting
import com.ertools.processing.signal.SignalPreprocessor.convectSpectrumToThirdsAmplitude
import com.ertools.processing.signal.SignalPreprocessor.convertSpectrumToAmplitude
import com.ertools.processing.signal.SignalPreprocessor.convertSpectrumToOctavesAmplitude
import com.ertools.processing.signal.SignalPreprocessor.convertToComplex
import com.ertools.processing.signal.SignalPreprocessor.fft
import com.ertools.processing.signal.SignalPreprocessor.toDecibels
import kotlin.concurrent.thread
import kotlin.math.min

class AudioRecorder (
    private val context: Context,
    private val recordingDelayMillis: Long = AppConstants.RECORDER_DELAY_MILLIS,
    private val recordingPeriodSeconds: Double = AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_PERIOD
) : SpectrumProvider, SoundDataProvider {
    val recorderBufferSize = AudioRecord.getMinBufferSize(
        ProcessingUtils.AUDIO_SAMPLING_RATE,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    ).let {
        var power = 1
        while (power * 2 <= it) { power *= 2 }
        power
    }
    val dataBufferSize = recordingPeriodSeconds.toInt() * ProcessingUtils.AUDIO_SAMPLING_RATE * 2
    private val dataBuffer = ByteArray(dataBufferSize)
    private var spectrum = DoubleArray(ProcessingUtils.AUDIO_OCTAVES_AMOUNT)
    private var recorder: AudioRecord? = null
    @Volatile
    private var isRecording = false


    /** API **/
    fun startRecording() {
        if(isRecording) return
        Log.i("AudioRecorder", "Start recording.")
        initRecorder()
        if(recorder == null) return
        recorder?.startRecording()
        isRecording = true
        readDataRoutine()
    }

    fun stopRecording() {
        Log.i("AudioRecorder", "Stop recording.")
        if(recorder == null) return
        isRecording = false
        recorder?.stop()
        recorder?.release()
        recorder = null
    }

    /** Private **/

    @SuppressLint("MissingPermission")
    private fun initRecorder() {
        recorder = if (!isPermissionsGained(context)) null
        else AudioRecord(
            MediaRecorder.AudioSource.MIC,
            ProcessingUtils.AUDIO_SAMPLING_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            recorderBufferSize
        )
    }

    private fun readDataRoutine() {
        thread {
            val shiftSize = min(
                recordingPeriodSeconds * recorderBufferSize,
                recorderBufferSize * recordingDelayMillis / 1000.0
            ).toInt()

            while (isRecording) {
                shiftAudioBuffer()
                val readSize = recorder?.read(dataBuffer, dataBufferSize - recorderBufferSize, recorderBufferSize)
                if (readSize == null || readSize == AudioRecord.ERROR_INVALID_OPERATION) {
                    Log.e("AudioRecorder", "Error reading data.")
                }
                Thread.sleep(recordingDelayMillis)
            }
        }
    }

    private fun shiftAudioBuffer() {
        if(recorderBufferSize > dataBuffer.size) return
        for (i in 0 until dataBufferSize - recorderBufferSize) {
            dataBuffer[i] = dataBuffer[i + recorderBufferSize]
        }
    }

    /** Spectrum provider **/
    override fun getAmplitudeSpectrum(): AmplitudeSpectrum =
        dataBuffer.sliceArray(0 until recorderBufferSize)
            .convertToComplex()
            .fft()
            .convertSpectrumToAmplitude()
            .toDecibels()
            .applyWeighting(AppConstants.RECORDER_WEIGHTING)

    override fun getOctavesAmplitudeSpectrum(): OctavesAmplitudeSpectrum =
        dataBuffer.sliceArray(0 until recorderBufferSize)
            .convertToComplex()
            .fft()
            .convertSpectrumToOctavesAmplitude()
            .toDecibels()
            .applyWeighting(AppConstants.RECORDER_WEIGHTING)

    override fun getThirdsAmplitudeSpectrum(): ThirdsAmplitudeSpectrum =
        dataBuffer.sliceArray(0 until recorderBufferSize)
            .convertToComplex()
            .fft()
            .convectSpectrumToThirdsAmplitude()
            .toDecibels()
            .applyWeighting(AppConstants.RECORDER_WEIGHTING)


    /** Sound data provider **/
    override fun getData(): ByteArray = dataBuffer
    override fun getPeriodSeconds(): Double = recordingPeriodSeconds
}