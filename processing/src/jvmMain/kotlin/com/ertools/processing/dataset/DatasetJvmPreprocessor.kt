package com.ertools.processing.dataset

import com.ertools.processing.commons.ImageDim
import org.jetbrains.kotlinx.dl.api.core.shape.TensorShape
import org.jetbrains.kotlinx.dl.api.preprocessing.Operation
import org.jetbrains.kotlinx.dl.api.preprocessing.pipeline
import org.jetbrains.kotlinx.dl.impl.preprocessing.image.grayscale
import org.jetbrains.kotlinx.dl.impl.preprocessing.image.resize
import org.jetbrains.kotlinx.dl.impl.preprocessing.image.toFloatArray
import org.jetbrains.kotlinx.dl.impl.preprocessing.rescale
import java.awt.image.BufferedImage

object DatasetJvmPreprocessor {
    fun getPreprocessingPipeline(
        dim: ImageDim,
    ): Operation<BufferedImage, Pair<FloatArray, TensorShape>> {
        return pipeline<BufferedImage>()
            .resize {
                outputWidth = dim.width.toInt()
                outputHeight = dim.height.toInt()
            }
            .grayscale()
            .toFloatArray {  }
            .rescale {
                scalingCoefficient = 255f
            }
    }
}