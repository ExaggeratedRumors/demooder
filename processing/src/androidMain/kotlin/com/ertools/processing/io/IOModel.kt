package com.ertools.processing.io
import android.content.Context
import com.ertools.processing.model.ModelConfiguration
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.io.IOException

object IOModel {
    fun loadModel(context: Context, configuration: ModelConfiguration): Interpreter {
        return try {
            val litertBuffer = FileUtil.loadMappedFile(context, configuration.modelName)
            Interpreter(litertBuffer, Interpreter.Options().apply {
                numThreads = configuration.threadCount
                useNNAPI = configuration.useNNAPI
            })
        } catch (e: IOException) {
            throw IOException("Failed to load TFLite model: ${e.message}")
        } catch (e: Exception) {
            throw Exception("Failed to create interpreter: ${e.message}")
        }
    }
}