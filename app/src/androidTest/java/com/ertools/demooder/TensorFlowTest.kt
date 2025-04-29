package com.ertools.demooder

import androidx.test.platform.app.InstrumentationRegistry
import android.content.Context
import android.content.res.AssetManager
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.processing.commons.Emotion
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.data.WavFile
import com.ertools.processing.signal.SignalPreprocessor.stft
import com.ertools.processing.signal.Windowing
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.tensorflow.lite.TensorFlowLite
import java.io.File
import java.io.FileOutputStream

@RunWith(AndroidJUnit4::class)
class TensorFlowTest {
    @Test
    fun `print tensorflow version`() {
        assertEquals("TensorFlow version.","2.18.0", TensorFlowLite.runtimeVersion())
    }

    @Test
    fun `read emotion classifier model`() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val classifier = EmotionClassifier()
        classifier.loadClassifier(context)
        assertEquals(true, classifier.isModelInitialized)
   }

    @Test
    fun `read stft from audio data`() {
        val audioFilename = "test_audio.wav"
        val context = InstrumentationRegistry.getInstrumentation().context
        val inputStream = context.assets.open(audioFilename)
        val file = File.createTempFile("temp_asset_", ".wav")
        file.deleteOnExit()
        FileOutputStream(file).use { output ->
            inputStream.copyTo(output)
        }
        val wavFile = WavFile.fromFile(file)

        val stft = stft(
            wavFile.data,
            ProcessingUtils.SPECTROGRAM_FRAME_SIZE,
            ProcessingUtils.SPECTROGRAM_STEP_SIZE,
            Windowing.WindowType.Hamming
        )
        assertEquals(283, stft.size)
    }

    @Test
    fun `predict emotion from audio data`() {
        val audioFilename = "test_audio.wav"
        val context = InstrumentationRegistry.getInstrumentation().context
        val inputStream = context.assets.open(audioFilename)
        val file = File.createTempFile("temp_asset_", ".wav")
        file.deleteOnExit()
        FileOutputStream(file).use { output ->
            inputStream.copyTo(output)
        }
        val wavFile = WavFile.fromFile(file)
        assertEquals(wavFile.data.size, wavFile.header.subchunk2Size)

        val classifier = EmotionClassifier()
        classifier.loadClassifier(context)
        assertEquals(true, classifier.isModelInitialized)

        val startTime = System.currentTimeMillis()
        classifier.predict(wavFile.data) { result ->
            val endTime = System.currentTimeMillis()
            assertEquals(result.size, Emotion.entries.size)
            println("R: Prediction result: $result")
            println("R: Prediction time: ${endTime - startTime} ms")
        }
    }

    @Test
    fun `detect voice from stft`() {
        val audioFilename = "test_audio.wav"
        val context = InstrumentationRegistry.getInstrumentation().context
        val inputStream = context.assets.open(audioFilename)
        val file = File.createTempFile("temp_asset_", ".wav")
        file.deleteOnExit()
        FileOutputStream(file).use { output ->
            inputStream.copyTo(output)
        }
        val wavFile = WavFile.fromFile(file)

        val speechDetector = SpeechDetector()
        speechDetector.loadModel(context)
        speechDetector.detectSpeech(wavFile.data) {
            val result = it
            println("R: Detection result: $result")
            assertEquals(true, result)
        }
    }

}