package com.ertools.model

import com.ertools.model.operation.evaluate
import com.ertools.model.operation.confusionMatrix
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.dataset.DatasetJvmPreprocessor
import com.ertools.processing.io.IOManager
import java.util.*

fun main() {
    /** Data **/
    val spectrogramDataPath = ProcessingUtils.DIR_CREMA_D
    val modelName = "DL_6c_3d_50e_128b"

    /** Test program **/
    println("I:\tStart load model program.")

    println("I:\tLoad test dataset.")
    val (data, dim) = IOManager.loadDataset(spectrogramDataPath, false) { dim ->
        DatasetJvmPreprocessor.getPreprocessingPipeline(dim)
    }
    println("R:\tTraining data size: ${data.xSize()}, shape: $dim.")

    println("I:\tLoad model $modelName")
    IOManager.loadModel(modelName).use {
        it.reshape(dim.width, dim.height, 1L)

        println("I:\tEvaluate CNN.")
        val result = it.evaluate(test = data)
        println("R:\t${"%.4f".format(Locale.ENGLISH, result)} accuracy")

        println("I:\tConfusion Matrix")
        it.confusionMatrix(testData = data)
    }

    println("I:\tEnd program.")
}