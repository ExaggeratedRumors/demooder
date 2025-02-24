package com.ertools.demooder.core.classifier

import android.content.Context
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxInferenceModel

object ClassifierIO {
    fun loadOnnxModel(context: Context, configuration: ClassifierConfiguration): OnnxInferenceModel {
        val modelBytes = context.assets.open(configuration.modelName).readBytes()
        val model = OnnxInferenceModel.load(modelBytes)
        return model
    }

}