package com.ertools.processing

import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.io.IOSoundData
import com.ertools.processing.data.WavFile
import com.ertools.processing.signal.SignalPreprocessor
import com.ertools.processing.signal.Windowing
import com.ertools.processing.spectrogram.SpectrogramSample
import org.junit.Test
import java.io.File

class CreateSpectrogramTest {

    @Test
    fun `create and save spectrogram`() {
        /** Pathing **/
        val resourcesPath = "src/jvmTest/resources/"
        val filename = "tess.wav"

        /** Files **/
        val testFile = File(resourcesPath, filename)
        val wavFile = WavFile.fromFile(testFile)
        println("R:\tRead wav header:\n${wavFile.header}")

        /** Parameters **/
        val frameSize = ProcessingUtils.SPECTROGRAM_FRAME_SIZE
        val stepSize = ProcessingUtils.SPECTROGRAM_STEP_SIZE
        val window = Windowing.WindowType.Hamming
        val sampleRate = ProcessingUtils.WAV_TARGET_SAMPLE_RATE

        /** Create spectrogram **/
        val stft = SignalPreprocessor.stft(
            wavFile.data,
            frameSize,
            stepSize,
            window
        )
        IOSoundData.saveSpectrogramSample(
            SpectrogramSample(stft, wavFile.fileName),
            "$resourcesPath/spectrograms/"
        )

        /** Resampling **/
        wavFile.resample(sampleRate)
        val stftResampled = SignalPreprocessor.stft(
            wavFile.data,
            frameSize,
            stepSize,
            window
        )
        IOSoundData.saveSpectrogramSample(
            SpectrogramSample(stftResampled, "${wavFile.fileName}_resampled"),
            "$resourcesPath/spectrograms/"
        )
    }
}