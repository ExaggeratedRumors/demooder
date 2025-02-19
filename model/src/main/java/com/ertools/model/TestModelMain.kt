package com.ertools.model

import com.ertools.model.evaluation.ClassifierEvaluator
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.dataset.DatasetJvmPreprocessor
import com.ertools.processing.io.IOManager
import org.jetbrains.kotlinx.dl.api.core.metric.Metrics
import org.jetbrains.kotlinx.dl.dataset.evaluate
import java.util.*

fun main() {
    /** Data **/
    val spectrogramDataPath = ProcessingUtils.DIR_CREMA_D
    val modelName = "DL_8c_3d_100e_32b_v2"

    /** Test program **/
    println("I:\tStart load model program.")

    println("I:\tLoad test dataset.")
    val (data, dim) = IOManager.loadDataset(spectrogramDataPath, false) { dim ->
        DatasetJvmPreprocessor.getPreprocessingPipeline(dim)
    }
    println("R:\tTraining data size: ${data.xSize()}, shape: $dim.")

    println("I:\tLoad model $modelName")
    IOManager.loadModel("${ProcessingUtils.DIR_MODEL_OUTPUT}/$modelName").use {
        it.reshape(dim.width, dim.height, 1L)

        println("I:\tEvaluate CNN.")
        val result = it.evaluate(dataset = data, metric = Metrics.ACCURACY)
        println("R:\t${"%.4f".format(Locale.ENGLISH, result)} accuracy")

        println("I:\tConfusion Matrix")
        println(ClassifierEvaluator.confusionMatrix(model = it, testData = data))
    }

    println("I:\tEnd program.")
}