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
        assertEquals("TensorFlow version.","2.20.0-dev0+selfbuilt", TensorFlowLite.runtimeVersion())
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
        val audioFilename = "SAD.wav"
        val expectedEmotion = Emotion.entries.first {
            it.name == audioFilename.removeSuffix(".wav").uppercase()
        }

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
        classifier.loadClassifier(context, false)
        assertEquals(true, classifier.isModelInitialized)

        val startTime = System.currentTimeMillis()
        classifier.predict(wavFile.data, wavFile.header.sampleRate) { result ->
            val endTime = System.currentTimeMillis()

            Log.i("TensorFlowTest", "Prediction result: $result")
            Log.i("TensorFlowTest", "Prediction time: ${endTime - startTime} ms")

            assertEquals(6, result.size)
            assertEquals(expectedEmotion, result[0].label)

        }
    }


    @Test
    fun `read speech detection model`() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val speechDetector = SpeechDetector()
        speechDetector.loadModel(context)
        assertEquals(true, speechDetector.isModelInitialized)
    }

    @Test
    fun `detect voice from audio data`() {
        val audioFiles = mapOf(
            "cremad14kHz.wav" to true,
            "ravdess48kHz.wav" to true,
            "tess24414Hz.wav" to true,
            "horn44100Hz.wav" to false,
            "noise44100Hz.wav" to false,
            "bonfire44100Hz.wav" to false
        )

        audioFiles.forEach { (audioFileName, expected) ->
            val context = InstrumentationRegistry.getInstrumentation().context
            val inputStream = context.assets.open(audioFileName)
            val file = File.createTempFile("temp_asset_", ".wav")
            file.deleteOnExit()
            FileOutputStream(file).use { output ->
                inputStream.copyTo(output)
            }
            val wavFile = WavFile.fromFile(file)
            Log.i("TensorFlowTest", "WavFile header: ${wavFile.header}")

            val speechDetector = SpeechDetector()
            speechDetector.loadModel(context)

            val startTime = System.currentTimeMillis()
            speechDetector.detectSpeech(wavFile.data, wavFile.header.sampleRate) {
                val result = it
                val endTime = System.currentTimeMillis()
                Log.i("TensorFlowTest", "Detection result: $result, expected result: $expected, detection time: ${endTime - startTime}ms")
                assertEquals(expected, result)
            }
        }
    }

}