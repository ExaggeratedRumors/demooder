package com.ertools.model

import com.ertools.model.network.CNNDeep
import com.ertools.model.operation.compile
import com.ertools.model.operation.evaluate
import com.ertools.model.operation.fit
import com.ertools.model.operation.predict
import com.ertools.model.operation.save
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.dataset.DatasetJvmPreprocessor
import com.ertools.processing.io.IOManager
import org.jetbrains.kotlinx.dl.impl.summary.logSummary

fun main() {
    /** Data **/
    val spectrogramData = ProcessingUtils.DIR_CREMA_D
    val epochs = 100
    val batchSize = 32
    val outputModelFilename = "DL_8c_3d_100e_32b"

    /** Train program **/
    println("I:\tStart program.")

    println("I:\tLoad and split dataset.")
    val (data, dim) = IOManager.loadDataset(
        dir = spectrogramData,
        shuffle = true
    ) { dim ->
        DatasetJvmPreprocessor.getPreprocessingPipeline(dim)
    }
    val (usable, _) = data.split(1.0) /* take 100% of samples */
    val (train, valid) = usable.split(0.85) /* 85% samples is training set, 20% of samples is validation set */


    println("R:\tTraining data size: ${train.xSize()}, validation data size: ${valid.xSize()}, full data size: ${data.xSize()}.")

    println("I:\tBuild CNN for spectrogram data.")
    val network = CNNDeep.build(dim)
    /*val network = vgg16(
        imageSize = (dim.width * dim.height),
        numberOfClasses = LabelsExtraction.Emotion.entries.size,
        numberOfInputChannels = 1,
        lastLayerActivation = Activations.Softmax
    )*/

    network.use {
        it.compile()

        println("I:\tFit CNN.")
        it.fit(train = train, validation = valid, epochs = epochs, batchSize = batchSize)

        it.logSummary()

        println("I:\tEvaluate CNN - train dataset.")
        val result = it.evaluate(test = train)
        println("R:\t$result accuracy")

        println("I:\tConfusion Matrix")
        it.predict(testData = train)

        println("I:\tEvaluate CNN - valid dataset.")
        val resulValid = it.evaluate(test = valid)
        println("R:\t$resulValid accuracy")

        println("I:\tConfusion Matrix")
        it.predict(testData = valid)

        println("I:\tSave model.")
        it.save(outputModelFilename)
        println("R:\tModel saved to directory ${ProcessingUtils.DIR_MODEL_OUTPUT}/$outputModelFilename")
    }

    println("I:\tEnd program.")
}