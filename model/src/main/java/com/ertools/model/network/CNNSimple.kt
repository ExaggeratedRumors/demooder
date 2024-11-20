package com.ertools.model.network

import com.ertools.processing.commons.ImageDim
import com.ertools.processing.commons.LabelsExtraction
import org.jetbrains.kotlinx.dl.api.core.Sequential
import org.jetbrains.kotlinx.dl.api.core.activation.Activations
import org.jetbrains.kotlinx.dl.api.core.initializer.GlorotUniform
import org.jetbrains.kotlinx.dl.api.core.initializer.HeNormal
import org.jetbrains.kotlinx.dl.api.core.initializer.HeUniform
import org.jetbrains.kotlinx.dl.api.core.layer.convolutional.Conv2D
import org.jetbrains.kotlinx.dl.api.core.layer.convolutional.ConvPadding
import org.jetbrains.kotlinx.dl.api.core.layer.core.Dense
import org.jetbrains.kotlinx.dl.api.core.layer.core.Input
import org.jetbrains.kotlinx.dl.api.core.layer.pooling.MaxPool2D
import org.jetbrains.kotlinx.dl.api.core.layer.regularization.Dropout
import org.jetbrains.kotlinx.dl.api.core.layer.reshaping.Flatten

object CNNSimple {
    fun build(dim: ImageDim): Sequential {
        return Sequential.of(
            Input(
                dim.height,
                dim.width,
                1L
            ),
            Conv2D(
                filters = 8,
                kernelSize = intArrayOf(3, 3),
                strides = intArrayOf(1, 1, 1, 1),
                activation = Activations.Relu,
                kernelInitializer = HeNormal(),
                biasInitializer = HeUniform(),
                padding = ConvPadding.SAME
            ),
            MaxPool2D(
                poolSize = intArrayOf(1, 2, 2, 1),
                strides = intArrayOf(1, 2, 2, 1),
                padding = ConvPadding.VALID
            ),
            Flatten(),
            Dropout(rate = 0.5f),
            Dense(
                outputSize = LabelsExtraction.Emotion.entries.size,
                activation = Activations.Linear,
                kernelInitializer = GlorotUniform(),
                biasInitializer = HeUniform()
            )
        )
    }
}