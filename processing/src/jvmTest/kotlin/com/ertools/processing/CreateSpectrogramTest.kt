package com.ertools.processing

import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.data.WavFile
import com.ertools.processing.io.IOSpectrogram
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
        val filename = "ravdess48kHz.wav"

        /** Files **/
        val testFile = File(resourcesPath, filename)
        val wavFile = WavFile.fromFile(testFile)
        println("R:\tRead wav header:\n${wavFile.header}")

        /** Parameters **/
        val frameSize = ProcessingUtils.SPECTROGRAM_FRAME_SIZE
        val stepSize = ProcessingUtils.SPECTROGRAM_STEP_SIZE
        val window = Windowing.WindowType.Hamming
        val targetSampleRate = 8000

        /** Create spectrogram **/
        val stft = SignalPreprocessor.stft(
            wavFile.data,
            frameSize,
            stepSize,
            window
        )
        IOSpectrogram.saveSpectrogramSample(
            SpectrogramSample(stft, wavFile.fileName),
            "$resourcesPath/spectrograms/"
        )

        /** Resampling **/
        wavFile.downsample(targetSampleRate)
        val stftResampled = SignalPreprocessor.stft(
            wavFile.data,
            frameSize,
            stepSize,
            window
        )
        IOSpectrogram.saveSpectrogramSample(
            SpectrogramSample(stftResampled, "${wavFile.fileName}_resampled"),
            "$resourcesPath/spectrograms/"
        )
    }
}