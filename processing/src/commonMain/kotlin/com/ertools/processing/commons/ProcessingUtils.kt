package com.ertools.processing.commons


object ProcessingUtils {
    /** Wav files **/
    const val WAV_HEADER_SIZE = 44
    const val WAV_ADDITIONAL_HEADER_SIZE = 34
    const val WAV_STRING_SIZE = 4
    const val WAV_MAX_LENGTH = 300000
    const val WAV_TARGET_SAMPLE_RATE = 16000
    const val WAV_AUGMENT_AMOUNT = 3
    const val WAV_AUGMENT_AFFIX = "AUG"

    /** Spectrogram **/
    const val SPECTROGRAM_BATCH_SIZE = 100
    const val SPECTROGRAM_FRAME_SIZE = 512
    const val SPECTROGRAM_STEP_SIZE = 256
    const val SPECTROGRAM_COLOR_RANGE = 255

    /** Audio **/
    const val AUDIO_SAMPLING_RATE = 44100
    const val AUDIO_FFT_SIZE = 1024
    const val AUDIO_THIRDS_AMOUNT = 33
    const val AUDIO_OCTAVES_AMOUNT = 11

    /** Constants **/
    const val EXT_WAV_FILE = "wav"
    const val EXT_SPECTROGRAM_OUTPUT = "png"
    const val FILE_SPECTROGRAMS_METADATA = "metadata"
}