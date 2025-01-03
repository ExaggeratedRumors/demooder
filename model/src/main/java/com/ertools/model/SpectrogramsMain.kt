package com.ertools.model

import com.ertools.processing.commons.LabelsExtraction
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.io.IOManager
import com.ertools.processing.signal.SignalPreprocessor
import com.ertools.processing.signal.Windowing
import com.ertools.processing.spectrogram.SpectrogramSample
import com.ertools.processing.spectrogram.SpectrogramsMetadata
import java.util.*
import kotlin.math.ceil
import kotlin.math.min

fun main () {
    /** Data **/
    val dataDir = ProcessingUtils.DIR_OWN_REC
    val spectrogramsBatchSize = ProcessingUtils.SPECTROGRAM_BATCH_SIZE
    val frameSize = ProcessingUtils.SPECTROGRAM_FRAME_SIZE
    val stepSize = ProcessingUtils.SPECTROGRAM_STEP_SIZE


    /** Program **/
    println("I:\tStart program.")

    println("I:\tLoad sound data.")
    val soundData = IOManager.loadWavFiles(
        wavDir = dataDir
    )
    println("R:\tRead ${soundData.size} samples.")

    println("I:\tProcess data to spectrograms.")
    val metadata = SpectrogramsMetadata()
    val sublistAmount = ceil(1.0f * soundData.size / spectrogramsBatchSize).toInt()
    (0 until sublistAmount).forEach {
        val start = it * spectrogramsBatchSize
        val end = min((it + 1) * spectrogramsBatchSize, soundData.size)
        val subset = soundData.subList(start, end)
        val spectrogramSet: List<SpectrogramSample> = SignalPreprocessor.convertWavFilesToSamples(
            wavFiles = subset,
            frameSize = frameSize,
            stepSize = stepSize,
            window = Windowing.WindowType.Hamming,
            filters = { labels ->
                labels.quality in listOf(
                    LabelsExtraction.Quality.XX,
                    LabelsExtraction.Quality.LO,
                    LabelsExtraction.Quality.MD,
                    LabelsExtraction.Quality.HI
                )
            }
        )
        IOManager.saveSpectrogramSamples(spectrogramSet, dataDir)
        metadata.update(spectrogramSet)
        println("R:\t${"%.1f".format(Locale.ENGLISH, 100.0 * end / soundData.size)}%/100%.")
    }
    IOManager.saveSpectrogramMetadata(metadata, dataDir)
    println("R:\tSaved ${metadata.dataAmount} spectrograms image to ${ProcessingUtils.DIR_SPECTROGRAMS_OUTPUT}/$dataDir")

    println("I:\tEnd program.")
}