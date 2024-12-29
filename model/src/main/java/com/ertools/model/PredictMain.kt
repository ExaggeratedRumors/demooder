package com.ertools.model

import com.ertools.processing.commons.ImageDim
import com.ertools.processing.commons.LabelsExtraction
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.dataset.DatasetJvmPreprocessor
import com.ertools.processing.io.IOManager
import com.ertools.processing.signal.SignalPreprocessor
import com.ertools.processing.signal.Windowing
import com.ertools.processing.spectrogram.SpectrogramsMetadata

fun main () {
    /** Data **/
    val dataDir = ProcessingUtils.DIR_OWN_REC
    val frameSize = ProcessingUtils.SPECTROGRAM_FRAME_SIZE
    val stepSize = ProcessingUtils.SPECTROGRAM_STEP_SIZE
    val modelName = "DL_4c_4d_50e_128b"
    val dim = ImageDim(136, 128)

    /** Predict program **/
    println("I:\tStart predicting program.")

    println("I:\tLoad sound data.")
    val soundData = IOManager.loadWavFiles(
        wavDir = dataDir
    )
    println("R:\tRead ${soundData.size} samples.")

    println("I:\tProcess data to spectrograms.")
    val spectrogramSet = SignalPreprocessor.processWavFiles(
        wavFiles = soundData,
        frameSize = frameSize,
        stepSize = stepSize,
        window = Windowing.WindowType.Hamming
    )
    val metadata = SpectrogramsMetadata()
    metadata.update(spectrogramSet)

    IOManager.saveSpectrogramSamples(spectrogramSet, dataDir)
    IOManager.saveSpectrogramMetadata(metadata, dataDir)
    println("R:\tProcess ${spectrogramSet.size} predict images and save to: $dataDir")

    println("I:\tLoad predict dataset.")
    val (data, _) = IOManager.loadDataset(dataDir, shuffle = false) { _ ->
        DatasetJvmPreprocessor.getPreprocessingPipeline(dim)
    }
    println("R:\tPredict data size: ${data.xSize()}.")

    println("I:\tLoad model.")
    IOManager.loadModel(modelName).use {
        it.reshape(dim.width, dim.height, 1L)

        println("I:\tStart predict.")
        val predictAmount = data.xSize()
        (0 until predictAmount).forEach { i ->
            val timeStart = System.nanoTime()
            val result = LabelsExtraction.Emotion.entries[it.predict(data.getX(i))]
            val timeDiff = (System.nanoTime() - timeStart) / 1000000
            println("R:\t[TRUE] ${LabelsExtraction.Emotion.entries[data.getY(i).toInt()]}, [PRED] $result, time: $timeDiff ms")
        }
    }

    println("I:\tEnd program.")
}