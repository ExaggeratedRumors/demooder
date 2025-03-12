package com.ertools.processing.dataset

import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.data.ImageDim
import org.jetbrains.kotlinx.dl.api.core.shape.TensorShape
import org.jetbrains.kotlinx.dl.api.preprocessing.Operation
import org.jetbrains.kotlinx.dl.api.preprocessing.pipeline
import org.jetbrains.kotlinx.dl.impl.preprocessing.image.resize
import org.jetbrains.kotlinx.dl.impl.preprocessing.image.toFloatArray
import org.jetbrains.kotlinx.dl.impl.preprocessing.onResult
import org.jetbrains.kotlinx.dl.impl.preprocessing.rescale
import java.awt.image.BufferedImage
import java.io.File

object DatasetJvmPreprocessor {
    fun getPreprocessingPipeline(
        dim: ImageDim,
    ): Operation<BufferedImage, Pair<FloatArray, TensorShape>> {
        return pipeline<BufferedImage>()
            .resize {
                outputWidth = dim.width.toInt()
                outputHeight = dim.height.toInt()
            }
            .onResult {
                /** save **/
                val outputDirectory = File(ProcessingUtils.DIR_SPECTROGRAMS_OUTPUT, "temp")
                val outputFile = File(outputDirectory, "spectrogram.jpg")
                outputFile.parentFile.mkdirs()
                outputFile.createNewFile()
                javax.imageio.ImageIO.write(it, "jpg", outputFile)
            }
            .toFloatArray {  }
            .rescale {
                scalingCoefficient = 255f
            }
    }
}