package com.ertools.processing.dataset


import android.graphics.Bitmap
import com.ertools.processing.data.ImageDim
import org.jetbrains.kotlinx.dl.api.core.shape.TensorShape
import org.jetbrains.kotlinx.dl.api.preprocessing.Operation
import org.jetbrains.kotlinx.dl.api.preprocessing.pipeline
import org.jetbrains.kotlinx.dl.impl.preprocessing.TensorLayout
import org.jetbrains.kotlinx.dl.impl.preprocessing.onResult
import org.jetbrains.kotlinx.dl.impl.preprocessing.rescale
import org.jetbrains.kotlinx.dl.impl.preprocessing.resize
import org.jetbrains.kotlinx.dl.impl.preprocessing.toFloatArray

object DatasetAndroidPreprocessor {
    fun getPreprocessingPipeline(
        dim: ImageDim,
    ): Operation<Bitmap, Pair<FloatArray, TensorShape>> {
        return pipeline<Bitmap>()
            .resize {
                outputWidth = dim.width.toInt()
                outputHeight = dim.height.toInt()
            }
            .toFloatArray {  }
            .rescale {
                scalingCoefficient = 255f
            }
    }
}