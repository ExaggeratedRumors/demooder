package com.ertools.processing.commons


object ProcessingUtils {
    /** Wav files **/
    const val WAV_HEADER_SIZE = 44
    const val WAV_ADDITIONAL_HEADER_SIZE = 34
    const val WAV_STRING_SIZE = 4
    const val WAV_MAX_LENGTH = 300000
    const val WAV_MAX_SAMPLE_RATE = 16000
    const val WAV_AUGMENT_AMOUNT = 3
    const val WAV_AUGMENT_AFFIX = "AUG"

    /** Spectrogram **/
    const val SPECTROGRAM_BATCH_SIZE = 256
    const val SPECTROGRAM_FRAME_SIZE = 512
    const val SPECTROGRAM_STEP_SIZE = 256
    const val SPECTROGRAM_COLOR_RANGE = 255

    /** Audio **/
    const val AUDIO_SAMPLING_RATE = 44100
    const val AUDIO_FFT_SIZE = 1024
    const val AUDIO_THIRDS_AMOUNT = 33

    /** Pathing **/
    const val EXT_WAV_FILE = "wav"
    const val EXT_SPECTROGRAM_OUTPUT = "png"
    const val DIR_CREMA_D = "CREMA-D/AudioWAV"
    const val DIR_OWN_REC = "OWN_REC"
    const val FILE_SPECTROGRAMS_METADATA = "metadata"
    const val DIR_AUDIO_INPUT = "data/data_audio"
    const val DIR_SPECTROGRAMS_OUTPUT = "data/data_spectrograms"
    const val DIR_MODEL_OUTPUT = "data/data_models"

    /** Dataset **/
    const val DATASET_TEST_RATIO = 0.0
    const val DATASET_VALID_RATIO = 0.25
    const val DATASET_SHUFFLE_ATTEMPTS = 100

    /** Model **/
    const val MODEL_DEFAULT_NAME = "SimpleModel"
    const val MODEL_EPOCHS = 1
    const val MODEL_BATCH_SIZE = 32
    const val MODEL_EARLY_STOP = false
}