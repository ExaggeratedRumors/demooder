package com.ertools.demooder

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.processing.commons.Emotion
import com.ertools.processing.data.WavFile
import junit.framework.TestCase.assertEquals
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
    fun `predict emotion from audio data`() {
        val audioFilename = "cremad14kHz.wav"
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
            Log.i("TensorFlowTest", "Prediction result: $result")
            Log.i("TensorFlowTest", "Prediction time: ${endTime - startTime} ms")
        }
    }


    @Test
    fun `read speech detection model`() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val sampleRate = 16000
        val speechDetector = SpeechDetector(sampleRate)
        speechDetector.loadModel(context)
        assertEquals(true, speechDetector.isModelInitialized)
    }

    @Test
    fun `detect voice from audio data`() {
        val audioFiles = mapOf(
            Pair("cremad14kHz.wav", 14000) to true,
            Pair("ravdess48kHz.wav", 48000) to true,
            Pair("tess24414Hz.wav", 24414) to true,
            Pair("horn44100Hz.wav", 44100) to false,
            Pair("noise44100Hz.wav", 44100) to false,
            Pair("bonfire44100Hz.wav", 44100) to false
        )

        audioFiles.forEach { (audioPair, expected) ->
            val context = InstrumentationRegistry.getInstrumentation().context
            val inputStream = context.assets.open(audioPair.first)
            val file = File.createTempFile("temp_asset_", ".wav")
            file.deleteOnExit()
            FileOutputStream(file).use { output ->
                inputStream.copyTo(output)
            }
            val wavFile = WavFile.fromFile(file)
            Log.i("TensorFlowTest", "WavFile header: ${wavFile.header}")

            val speechDetector = SpeechDetector(audioPair.second)
            speechDetector.loadModel(context)
            speechDetector.detectSpeech(wavFile.data) {
                val result = it
                Log.i("TensorFlowTest", "Detection result: $result")
                assertEquals(expected, result)
            }
        }
    }

}