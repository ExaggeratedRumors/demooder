package com.ertools.demooder

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ertools.demooder.core.recorder.AudioRecorder
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AudioRecorderTest {
    @Test
    fun `periodic buffer shift correctly`() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val recorder = AudioRecorder(context)
    }
}