package com.ertools.demooder

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ertools.demooder.core.recorder.AudioRecorder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AudioRecorderTest {
    private lateinit var audioRecorder: AudioRecorder
    val samplingRate = 44100
    private val recordingPeriodSeconds = 5.0

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        audioRecorder = AudioRecorder(
            context = context,
            recordingPeriodSeconds = recordingPeriodSeconds,
            recordingDelayMillis = 0
        )
    }

    @Test
    fun `periodic buffer shift correctly`() {
        val bufferSize = audioRecorder.recorderBufferSize
        println("bufferSize = $bufferSize")
        assert(bufferSize == 2048)
    }
}