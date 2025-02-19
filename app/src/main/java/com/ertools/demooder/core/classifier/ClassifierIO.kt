package com.ertools.demooder.core.classifier

import android.content.Context
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxInferenceModel

object ClassifierIO {
    fun loadModel(context: Context) {
        val modelBytes = context.assets.open("model.onnx").readBytes()
        val model = OnnxInferenceModel.load(modelBytes)


    }

}