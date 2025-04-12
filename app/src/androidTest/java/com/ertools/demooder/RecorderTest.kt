package com.ertools.demooder

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ertools.demooder.core.recorder.AudioRecorder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecorderTest {
    private lateinit var audioRecorder: AudioRecorder

    @Before
    fun setUp() {
        audioRecorder = AudioRecorder()
    }

    @Test
    fun `periodic buffer shift correctly`() {
        val bufferSize = audioRecorder.recorderBufferSize
        println("bufferSize = $bufferSize")
        assert(bufferSize == 2048)
    }
}