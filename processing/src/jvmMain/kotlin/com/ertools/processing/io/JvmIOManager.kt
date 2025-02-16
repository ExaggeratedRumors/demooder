package com.ertools.processing.io

import com.ertools.processing.commons.ProcessingUtils
import org.jetbrains.kotlinx.dl.api.inference.TensorFlowInferenceModel
import java.io.File

object JvmIOManager {
    fun loadModel(modelName: String) = TensorFlowInferenceModel.load(File(ProcessingUtils.DIR_MODEL_OUTPUT, modelName))
}