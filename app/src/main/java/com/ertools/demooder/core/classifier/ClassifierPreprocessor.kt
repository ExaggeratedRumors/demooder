package com.ertools.demooder.core.classifier

import com.ertools.demooder.utils.AppConstants.APP_DIR_NAME
import com.ertools.processing.commons.RawData
import com.ertools.processing.model.ModelShape
import com.ertools.processing.signal.SignalPreprocessor
import com.ertools.processing.spectrogram.SpectrogramConfiguration
import com.ertools.processing.spectrogram.SpectrogramImage
import java.nio.ByteBuffer

class ClassifierPreprocessor(
    private val spectrogramConfiguration: SpectrogramConfiguration,
    private val modelShape: ModelShape
) {
    /*********/
    /** API **/
    /*********/

    /**
     * Preprocess the raw data to a ByteBuffer.
     * @param rawData The raw audio data to preprocess.
     * @param debug If true, saves the spectrogram image for debugging.
     */
    fun proceed(rawData: RawData, debug: Boolean = false): ByteBuffer {
        /*  val downSampled = rawData.downSampling(
            length = rawData.size,
            isStereo = false,
            inputFrequency = ProcessingUtils.AUDIO_SAMPLING_RATE,
            outputFrequency = ProcessingUtils.WAV_TARGET_SAMPLE_RATE
        )*/
        val stft = SignalPreprocessor.stft(
            rawData,
            spectrogramConfiguration.frameSize,
            spectrogramConfiguration.frameStep,
            spectrogramConfiguration.windowing
        )
        val bitmap = SpectrogramImage.fromSpectrogram(stft)
        if(debug) SpectrogramImage.saveSpectrogramImage(bitmap, APP_DIR_NAME)
        val inputBuffer = SpectrogramImage.scaledByteBufferFromBitmap(bitmap, modelShape)
        return inputBuffer
    }
}