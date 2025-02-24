package com.ertools.model.network

import com.ertools.processing.data.ImageDim
import com.ertools.processing.data.LabelsExtraction
import org.jetbrains.kotlinx.dl.api.core.Sequential
import org.jetbrains.kotlinx.dl.api.core.activation.Activations
import org.jetbrains.kotlinx.dl.api.core.initializer.GlorotUniform
import org.jetbrains.kotlinx.dl.api.core.layer.convolutional.Conv2D
import org.jetbrains.kotlinx.dl.api.core.layer.core.Dense
import org.jetbrains.kotlinx.dl.api.core.layer.core.Input
import org.jetbrains.kotlinx.dl.api.core.layer.pooling.MaxPool2D
import org.jetbrains.kotlinx.dl.api.core.layer.regularization.Dropout
import org.jetbrains.kotlinx.dl.api.core.layer.reshaping.Flatten

/** Attempt to replicate a VGG network architecture **/
object CNNDeep {
    fun build(dim: ImageDim): Sequential {
        return Sequential.of(
            Input(dim.width, dim.height, 3L),
            Conv2D(
                filters = 32,
                kernelSize = intArrayOf(3, 3),
                activation = Activations.Relu,
            ),
            Conv2D(
                filters = 32,
                kernelSize = intArrayOf(3, 3),
                activation = Activations.Relu
            ),
            MaxPool2D(
                poolSize = intArrayOf(1, 2, 2, 1),
            ),
            Conv2D(
                filters = 64,
                kernelSize = intArrayOf(3, 3),
                activation = Activations.Relu,
            ),
            Conv2D(
                filters = 64,
                kernelSize = intArrayOf(3, 3),
                activation = Activations.Relu,
            ),
            MaxPool2D(
                poolSize = intArrayOf(1, 2, 2, 1),
            ),
            Conv2D(
                filters = 128,
                kernelSize = intArrayOf(3, 3),
                activation = Activations.Relu,
            ),
            Conv2D(
                filters = 128,
                kernelSize = intArrayOf(3, 3),
                activation = Activations.Relu,
            ),
            MaxPool2D(
                poolSize = intArrayOf(1, 2, 2, 1),
            ),
            Conv2D(
                filters = 256,
                kernelSize = intArrayOf(3, 3),
                activation = Activations.Relu,
            ),
            Conv2D(
                filters = 256,
                kernelSize = intArrayOf(3, 3),
                activation = Activations.Relu,
            ),
            MaxPool2D(
                poolSize = intArrayOf(1, 2, 2, 1),
            ),
            Flatten(),
            Dense(
                outputSize = 64,
                activation = Activations.Relu,
            ),
            Dropout(rate = 0.25f),
            Dense(
                outputSize = 64,
                activation = Activations.Relu,
            ),
            Dropout(rate = 0.25f),
            Dense(
                outputSize = LabelsExtraction.Emotion.entries.size,
                activation = Activations.Softmax,
                kernelInitializer = GlorotUniform(),
            )
        )
    }
}