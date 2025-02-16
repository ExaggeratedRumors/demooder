package com.ertools.processing.io

import android.content.Context
import org.jetbrains.kotlinx.dl.api.inference.TensorFlowInferenceModel
import java.io.File
import java.io.FileOutputStream

object AndroidIOManager {
    fun loadModel(context: Context, modelName: String): TensorFlowInferenceModel {
        val modelDir = File(context.filesDir, modelName).apply { mkdirs() }
        val files = mutableListOf<File>()

        context.assets.list(modelName)?.forEach {
            context.assets.open("$modelName/$it").use { input ->
                val tempFile = File(modelDir, it)
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
                files.add(tempFile)
            }
        }

        //return TensorFlowInferenceModel.load(modelDir)
        return TensorFlowInferenceModel.load(File(context.filesDir, modelName))
    }
}