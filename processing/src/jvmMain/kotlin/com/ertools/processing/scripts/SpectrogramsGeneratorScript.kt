package com.ertools.processing.scripts

import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.commons.ProjectPathing
import com.ertools.processing.data.DataSource
import com.ertools.processing.io.IOManager
import com.ertools.processing.signal.Windowing
import com.ertools.processing.spectrogram.SpectrogramSample
import com.ertools.processing.spectrogram.SpectrogramsMetadata
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.min

fun main (args: Array<String>) {
    /** Data **/
    val dataDir = if(args.isNotEmpty()) args[0] else ProjectPathing.DIR_DATA_RAVDESS
    val spectrogramsBatchSize = args.getOrNull(1)?.toInt() ?: ProcessingUtils.SPECTROGRAM_BATCH_SIZE
    val frameSize = args.getOrNull(2)?.toInt() ?: ProcessingUtils.SPECTROGRAM_FRAME_SIZE
    val stepSize = args.getOrNull(1)?.toInt() ?: ProcessingUtils.SPECTROGRAM_STEP_SIZE
    val dataSource = DataSource.sourceFromDirectoryName(dataDir)

    /** Program **/
    println("I:\tStart program.")

    println("I:\tLoad sound data.")
    val soundData = IOManager.loadWavFiles(
        wavDir = dataDir
    )
    println("R:\tRead ${soundData.size} samples.")

    println("I:\tDown-sampling data to ${ProcessingUtils.WAV_TARGET_SAMPLE_RATE}kHz.")
    soundData.forEach {
        it.resample(ProcessingUtils.WAV_TARGET_SAMPLE_RATE)
    }

    println("I:\tProcess data to spectrograms.")
    val metadata = SpectrogramsMetadata()
    val sublistAmount = ceil(1.0f * soundData.size / spectrogramsBatchSize).toInt()
    (0 until sublistAmount).forEach {
        println("R:\tCreating spectrograms... [progress ${"%.1f".format(Locale.ENGLISH, 100.0 * spectrogramsBatchSize * it / soundData.size)}%]")
        val start = it * spectrogramsBatchSize
        val end = min((it + 1) * spectrogramsBatchSize, soundData.size)
        val subset = soundData.subList(start, end)
        val spectrogramSet: List<SpectrogramSample> = IOManager.convertWavFilesToSpectrogramSamples(
            wavFiles = subset,
            frameSize = frameSize,
            stepSize = stepSize,
            window = Windowing.WindowType.Hamming
        )
        IOManager.saveSpectrogramSamples(spectrogramSet, dataDir)
        metadata.update(spectrogramSet)
    }
    println("R:\tSaved ${metadata.dataAmount} spectrograms images to ${ProjectPathing.DIR_SPECTROGRAMS_OUTPUT}/$dataDir directory.")
    IOManager.saveSpectrogramMetadata(metadata, dataDir)
    println("R:\tGenerated spectrograms metadata file ${ProcessingUtils.FILE_SPECTROGRAMS_METADATA} in ${ProjectPathing.DIR_SPECTROGRAMS_OUTPUT}/$dataDir directory.")

    println("I:\tEnd program.")
}