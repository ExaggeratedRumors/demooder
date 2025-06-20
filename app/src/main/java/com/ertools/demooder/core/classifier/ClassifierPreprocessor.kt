package com.ertools.demooder.core.classifier

import com.ertools.demooder.utils.AppConstants.APP_DIR_NAME
import com.ertools.processing.commons.RawData
import com.ertools.processing.commons.Spectrogram
import com.ertools.processing.model.ModelShape
import com.ertools.processing.signal.Resampling.downSampling
import com.ertools.processing.signal.SignalPreprocessor
import com.ertools.processing.spectrogram.SpectrogramConfiguration
import com.ertools.processing.spectrogram.SpectrogramImage
import java.nio.ByteBuffer
import kotlin.math.min

/**
 * Preprocess the raw data of 16-bits PCM signal to a list of ByteBuffer.
 * @param spectrogramConfiguration The configuration for the spectrogram.
 * @param modelShape The shape of the model input.
 * @param targetSampleRate The target sample rate for the input data.
 * @param debugMode Whether to enable debug mode for saving spectrogram images.
 */
class ClassifierPreprocessor(
    private val spectrogramConfiguration: SpectrogramConfiguration,
    private val modelShape: ModelShape,
    private val targetSampleRate: Int,
    private val debugMode: Boolean = false
) {
    /*********/
    /** API **/
    /*********/

    /**
     * Preprocess the raw 16-bit PCM data to a list of put in ByteBuffer spectrograms.
     * @param rawData The raw audio data to preprocess.
     * @param sampleRate The sample rate of the input audio data.
     */
    fun proceed(rawData: RawData, sampleRate: Int): List<ByteBuffer> {
        require(rawData.size % 2 == 0) { "Input data must be 16-bits PCM signal." }
        val downSampled = rawData.downSampling(
            length = rawData.size,
            isStereo = false,
            inputFrequency = sampleRate,
            outputFrequency = targetSampleRate
        )

        val spectrogramsAmount = downSampled.size / 2 / targetSampleRate
        if(spectrogramsAmount == 0) throw IllegalStateException("Preprocessor input data is too short.")
        val slicedDataSize = downSampled.size / spectrogramsAmount
        val spectrograms: List<ByteBuffer> = (0 until spectrogramsAmount).map {
            val start = it * slicedDataSize
            val end = min(start + slicedDataSize, downSampled.size)
            createSpectrogramImage(downSampled.sliceArray(start until end))
        }
        return spectrograms
    }

    /*************/
    /** Private **/
    /*************/
    private fun createSpectrogramImage(data: RawData): ByteBuffer {
        val stft: Spectrogram = SignalPreprocessor.stft(
            data,
            spectrogramConfiguration.frameSize,
            spectrogramConfiguration.frameStep,
            spectrogramConfiguration.windowing
        )
        val bitmap = SpectrogramImage.fromSpectrogram(stft)
        if(debugMode) SpectrogramImage.saveSpectrogramImage(bitmap, APP_DIR_NAME)
        return SpectrogramImage.scaledByteBufferFromBitmap(bitmap, modelShape)
    }
}