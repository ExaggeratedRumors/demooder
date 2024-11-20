package com.ertools.model.network

import com.ertools.processing.commons.ImageDim
import com.ertools.processing.commons.LabelsExtraction
import org.jetbrains.kotlinx.dl.api.core.Sequential
import org.jetbrains.kotlinx.dl.api.core.activation.Activations
import org.jetbrains.kotlinx.dl.api.core.initializer.GlorotUniform
import org.jetbrains.kotlinx.dl.api.core.layer.convolutional.Conv2D
import org.jetbrains.kotlinx.dl.api.core.layer.core.Dense
import org.jetbrains.kotlinx.dl.api.core.layer.core.Input
import org.jetbrains.kotlinx.dl.api.core.layer.pooling.MaxPool2D
import org.jetbrains.kotlinx.dl.api.core.layer.regularization.Dropout
import org.jetbrains.kotlinx.dl.api.core.layer.reshaping.Flatten

/** This is almost VGG network architecture **/
object CNNDeep {
    fun build(dim: ImageDim): Sequential {
        return Sequential.of(
            Input(dim.width, dim.height, 1L),
            Conv2D(
                filters = 16,
                kernelSize = intArrayOf(3, 3),
                activation = Activations.Relu,
            ),
            Conv2D(
                filters = 16,
                kernelSize = intArrayOf(3, 3),
                activation = Activations.Relu
            ),
            MaxPool2D(
                poolSize = intArrayOf(1, 2, 2, 1),
            ),
            Dropout(rate = 0.2f),
            Conv2D(
                filters = 32,
                kernelSize = intArrayOf(3, 3),
                activation = Activations.Relu,
            ),
            Conv2D(
                filters = 32,
                kernelSize = intArrayOf(3, 3),
                activation = Activations.Relu,
            ),
            MaxPool2D(
                poolSize = intArrayOf(1, 2, 2, 1),
            ),
            Dropout(rate = 0.2f),
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
            Dropout(rate = 0.2f),
            Flatten(),
            Dense(
                outputSize = 64,
                activation = Activations.Relu,
            ),
            Dropout(rate = 0.2f),
            Dense(
                outputSize = 64,
                activation = Activations.Relu,
            ),
            Dropout(rate = 0.5f),
            Dense(
                outputSize = LabelsExtraction.Emotion.entries.size,
                activation = Activations.Softmax,
                kernelInitializer = GlorotUniform(),
            )
        )
    }
}