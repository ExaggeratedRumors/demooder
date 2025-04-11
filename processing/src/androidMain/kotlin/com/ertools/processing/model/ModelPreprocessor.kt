package com.ertools.processing.model

import android.graphics.Bitmap
import com.ertools.processing.commons.RawData
import com.ertools.processing.signal.SignalPreprocessor
import com.ertools.processing.spectrogram.SpectrogramImage
import java.nio.ByteBuffer

class ModelPreprocessor(
    private val modelConfiguration: ModelConfiguration,
    private val modelShape: ModelShape
) {
    /*********/
    /** API **/
    /*********/

    /**
     * Preprocess the raw data to a ByteBuffer.
     *
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
            modelConfiguration.frameSize,
            modelConfiguration.frameStep,
            modelConfiguration.windowing
        )
        val bitmap = SpectrogramImage.fromSpectrogram(stft)
        if(debug) SpectrogramImage.saveSpectrogramImage(bitmap, "ertools")
        val inputBuffer = SpectrogramImage.scaledByteBufferFromBitmap(bitmap, modelShape)
        return inputBuffer
    }
}