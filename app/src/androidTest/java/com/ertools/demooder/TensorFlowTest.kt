package com.ertools.demooder

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.processing.commons.Emotion
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.commons.Spectrogram
import com.ertools.processing.data.WavFile
import com.ertools.processing.signal.SignalPreprocessor.stft
import com.ertools.processing.signal.Windowing
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.tensorflow.lite.TensorFlowLite
import java.io.File

@RunWith(AndroidJUnit4::class)
class TensorFlowTest {

    @Before
    fun setUp() {

    }

    @Test
    fun `print tensorflow version`() {
        assertEquals("TensorFlow version.","1.15.0", TensorFlowLite.runtimeVersion())
    }

    @Test
    fun `read emotion classifier model`(): EmotionClassifier {
        val context = getApplicationContext<Context>()
        val classifier = EmotionClassifier()
        classifier.loadClassifier(context)
        assertEquals(true, classifier.isModelInitialized)
        return classifier
    }

    @Test
    fun `read stft from audio data`(): Spectrogram {
        val audioFilename = "test_audio.wav"
        val wavFile = WavFile.fromFile(File(audioFilename))
        val stft = stft(
            wavFile.data,
            ProcessingUtils.SPECTROGRAM_FRAME_SIZE,
            ProcessingUtils.SPECTROGRAM_STEP_SIZE,
            Windowing.WindowType.Hamming
        )
        assertEquals(stft.size, wavFile.data.size)
        return stft
    }

}