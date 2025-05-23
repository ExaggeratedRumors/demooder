package com.ertools.processing.scripts

import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.commons.ProjectPathing
import com.ertools.processing.io.IOSoundData
import com.ertools.processing.io.IOSpectrogram
import com.ertools.processing.signal.Windowing
import com.ertools.processing.spectrogram.SpectrogramSample
import com.ertools.processing.spectrogram.SpectrogramsMetadata
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.min

fun main(args: Array<String>) {
    /** Data **/
    val dataDirs = if(args.isNotEmpty()) args.toList()
    else listOf(
        ProjectPathing.DIR_DATA_CREMA_D,
        ProjectPathing.DIR_DATA_RAVDESS,
        ProjectPathing.DIR_DATA_SAVEE,
        ProjectPathing.DIR_DATA_TESS
    )

    println("I:\tStart program.")

    /** Common configuration **/
    val frameSize = ProcessingUtils.SPECTROGRAM_FRAME_SIZE
    val stepSize = ProcessingUtils.SPECTROGRAM_STEP_SIZE
    val resampling = false

    /** Program **/
    for(dataDir in dataDirs) {
        println("\n\nI:\tLoad sound data from directory: $dataDir")
        val soundData = IOSoundData.loadWavFiles(
            wavDir = dataDir
        )
        println("R:\tRead ${soundData.size} samples.")

        if(resampling) {
            println("I:\tDown-sampling data to ${ProcessingUtils.WAV_TARGET_SAMPLE_RATE}kHz.")
            soundData.forEach {
                it.resample(ProcessingUtils.WAV_TARGET_SAMPLE_RATE)
            }
        }

        println("I:\tProcess data to spectrograms.")
        val metadata = SpectrogramsMetadata()
        val spectrogramsBatchSize = ProcessingUtils.SPECTROGRAM_BATCH_SIZE
        val sublistAmount = ceil(1.0f * soundData.size / spectrogramsBatchSize).toInt()
        (0 until sublistAmount).forEach {
            println(
                "R:\tCreating spectrograms... [progress ${
                    "%.1f".format(
                        Locale.ENGLISH,
                        100.0 * spectrogramsBatchSize * it / soundData.size
                    )
                }%]"
            )
            val start = it * spectrogramsBatchSize
            val end = min((it + 1) * spectrogramsBatchSize, soundData.size)
            val subset = soundData.subList(start, end)
            val spectrogramSet: List<SpectrogramSample> =
                IOSoundData.convertWavFilesToSpectrogramSamples(
                    wavFiles = subset,
                    frameSize = frameSize,
                    stepSize = stepSize,
                    window = Windowing.WindowType.Hamming
                )
            IOSpectrogram.saveSpectrogramSamples(spectrogramSet, dataDir)
            metadata.update(spectrogramSet)
        }
        println("R:\tSaved spectrograms images to ${ProjectPathing.DIR_SPECTROGRAMS_OUTPUT}/$dataDir directory.")
        IOSpectrogram.saveSpectrogramMetadata(metadata, dataDir)
        println("R:\tSpectrograms metadata:\n$metadata")
    }
    println("I:\tEnd program.")
}