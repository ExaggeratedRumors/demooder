package com.ertools.model

import com.ertools.processing.ModelShape
import com.ertools.processing.data.ImageDim
import com.ertools.processing.data.LabelsExtraction
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.dataset.DatasetJvmPreprocessor
import com.ertools.processing.io.IOManager
import com.ertools.processing.signal.SignalPreprocessor
import com.ertools.processing.signal.Windowing
import com.ertools.processing.spectrogram.SpectrogramsMetadata
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxInferenceModel
import org.jetbrains.kotlinx.dl.onnx.inference.executionproviders.ExecutionProvider
import org.jetbrains.kotlinx.dl.onnx.inference.inferAndCloseUsing
import java.io.File

fun main (args: Array<String>) {
    /** Data **/
    val dataDir = ProcessingUtils.DIR_OWN_REC
    val frameSize = ProcessingUtils.SPECTROGRAM_FRAME_SIZE
    val stepSize = ProcessingUtils.SPECTROGRAM_STEP_SIZE
    val modelName = "DL_8c_3d_10e_32b"
    val defaultOnnxModelPath = "data/data_models/$modelName/model.onnx"

    /** Predict program **/
    println("I:\tStart predicting program.")
    println("I:\tLoad sound data.")
    val soundData = IOManager.loadWavFiles(
        wavDir = dataDir,
        maxNumberOfFiles = 12
    )
    println("R:\tRead ${soundData.size} samples.")

    println("I:\tProcess data to spectrograms.")
    val spectrogramSet = SignalPreprocessor.convertWavFilesToSpectrogramSamples(
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
    val onnxModelPath = if(args.isEmpty()) defaultOnnxModelPath else args[0]
    if(!File(onnxModelPath).exists()) return println("E:\tModel file $onnxModelPath not found.")
    val model = OnnxInferenceModel(onnxModelPath)
    model.initializeWith(ExecutionProvider.CPU())
    val shape = ModelShape.fromShapeArray(model.inputDimensions)
    val (data, _) = IOManager.loadDataset(dataDir, shuffle = false) { _ ->
        DatasetJvmPreprocessor.getPreprocessingPipeline(ImageDim(shape.width, shape.height))
    }
    println("R:\tPredict data size: ${data.xSize()}.")

    println("I:\tLoad model.")
    model.inferAndCloseUsing(ExecutionProvider.CPU()) {
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