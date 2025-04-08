package com.ertools.demooder.core.recorder

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.ertools.demooder.utils.AppConstants
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
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

class AudioRecorder (
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
    private val dataBufferSize = recordingPeriodSeconds.toInt() * ProcessingUtils.AUDIO_SAMPLING_RATE * 2
    private val dataBuffer = ByteArray(dataBufferSize)
    private var recorder: AudioRecord? = null
    private lateinit var dataFlow: Flow<ByteArray>

    @Volatile
    private var isRecording = false

    /** API **/
    fun startRecording() {
        if(isRecording) return
        Log.i("AudioRecorder", "Start recording.")
        startRecordingFlow()
        if(recorder == null) return
        isRecording = true
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
        recorder = AudioRecord(
            MediaRecorder.AudioSource.VOICE_RECOGNITION,
            ProcessingUtils.AUDIO_SAMPLING_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            recorderBufferSize
        )
    }

    @SuppressLint("MissingPermission")
    private fun startRecordingFlow() {
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            ProcessingUtils.AUDIO_SAMPLING_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            recorderBufferSize
        )
        if(recorder?.state != AudioRecord.STATE_INITIALIZED) {
            Log.e("AudioRecorder", "AudioRecord failed to initialize.")
            return
        }

        dataFlow = flow {
            val dataBuffer = ByteArray(dataBufferSize)
            recorder?.startRecording()
            while (currentCoroutineContext().isActive && isRecording) {
                shiftAudioBuffer()
                val result = recorder!!.read(
                    dataBuffer,
                    dataBufferSize - recorderBufferSize,
                    recorderBufferSize
                )
                when(result) {
                    recorderBufferSize -> emit(dataBuffer)
                    AudioRecord.ERROR -> {
                        Log.w("AudioRecorder", "AudioRecord.ERROR")
                    }
                    AudioRecord.ERROR_BAD_VALUE -> {
                        Log.w("AudioRecorder", "AudioRecord.ERROR_BAD_VALUE")
                    }
                    AudioRecord.ERROR_INVALID_OPERATION -> {
                        Log.w("AudioRecorder", "AudioRecord.ERROR_INVALID_OPERATION")
                    }
                    AudioRecord.ERROR_DEAD_OBJECT -> {
                        Log.w("AudioRecorder", "AudioRecord.ERROR_DEAD_OBJECT")
                    }
                }
                delay(recordingDelayMillis)
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
    override suspend fun getAmplitudeSpectrum(): AmplitudeSpectrum =
        dataFlow.first().sliceArray(dataBufferSize - recorderBufferSize until dataBufferSize)
            .convertToComplex()
            .fft()
            .convertSpectrumToAmplitude()
            .toDecibels()
            .applyWeighting(AppConstants.RECORDER_WEIGHTING)

    override suspend fun getOctavesAmplitudeSpectrum(): OctavesAmplitudeSpectrum =
        dataFlow.first().sliceArray(dataBufferSize - recorderBufferSize until dataBufferSize)
            .convertToComplex()
            .fft()
            .convertSpectrumToOctavesAmplitude()
            .toDecibels()
            .applyWeighting(AppConstants.RECORDER_WEIGHTING)

    override suspend fun getThirdsAmplitudeSpectrum(): ThirdsAmplitudeSpectrum =
        dataFlow.first().sliceArray(dataBufferSize - recorderBufferSize until dataBufferSize)
            .convertToComplex()
            .fft()
            .convectSpectrumToThirdsAmplitude()
            .toDecibels()
            .applyWeighting(AppConstants.RECORDER_WEIGHTING)


    /** Sound data provider **/
    override suspend fun getData(): ByteArray = dataFlow.first()
    override suspend fun getPeriodSeconds(): Double = recordingPeriodSeconds
}