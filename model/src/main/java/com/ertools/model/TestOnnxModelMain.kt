package com.ertools.model

import com.ertools.model.evaluation.ClassifierEvaluator
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.dataset.DatasetJvmPreprocessor
import com.ertools.processing.io.IOManager
import org.jetbrains.kotlinx.dl.api.core.metric.Metrics
import org.jetbrains.kotlinx.dl.dataset.evaluate
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxInferenceModel
import org.jetbrains.kotlinx.dl.onnx.inference.executionproviders.ExecutionProvider
import org.jetbrains.kotlinx.dl.onnx.inference.inferAndCloseUsing
import java.io.File
import java.util.Locale

fun main(args: Array<String>) {
    /** Data **/
    val spectrogramDataPath = ProcessingUtils.DIR_CREMA_D
    val defaultOnnxModelPath = "data/data_models/DL_8c_3d_100e_32b/model"

    /** Test onnx program **/
    println("I:\tStart load model program.")

    println("I:\tLoad test dataset.")
    val (data, dim) = IOManager.loadDataset(spectrogramDataPath, false) { dim ->
        DatasetJvmPreprocessor.getPreprocessingPipeline(dim)
    }
    println("R:\tData size: ${data.xSize()}, shape: ${dim.width}W x ${dim.height}H")

    println("I:\tLoad model $defaultOnnxModelPath")
    val onnxModelPath = if(args.isEmpty()) defaultOnnxModelPath else args[0]
    if(!File(onnxModelPath).exists()) return println("E:\tModel file $onnxModelPath not found.")

    val model = OnnxInferenceModel(onnxModelPath)
    model.inferAndCloseUsing(ExecutionProvider.CPU()) {
        it.reshape(dim.width, dim.height, 1L)
        println("I:\tEvaluate CNN.")
        val result = it.evaluate(dataset = data, metric = Metrics.ACCURACY)
        println("R:\t${"%.4f".format(Locale.ENGLISH, result)} accuracy")

        println("I:\tConfusion Matrix")
        println(ClassifierEvaluator.confusionMatrix(model = it, testData = data))
    }

}