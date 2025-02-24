package com.ertools.model

import com.ertools.model.evaluation.ClassifierEvaluator
import com.ertools.model.network.CNNDeep
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.dataset.DatasetJvmPreprocessor
import com.ertools.processing.io.IOManager
import org.jetbrains.kotlinx.dl.api.core.SavingFormat
import org.jetbrains.kotlinx.dl.api.core.WritingMode
import org.jetbrains.kotlinx.dl.api.core.callback.Callback
import org.jetbrains.kotlinx.dl.api.core.callback.EarlyStopping
import org.jetbrains.kotlinx.dl.api.core.callback.EarlyStoppingMode
import org.jetbrains.kotlinx.dl.api.core.history.EpochTrainingEvent
import org.jetbrains.kotlinx.dl.api.core.loss.Losses
import org.jetbrains.kotlinx.dl.api.core.metric.Metrics
import org.jetbrains.kotlinx.dl.api.core.optimizer.Adam
import org.jetbrains.kotlinx.dl.api.core.optimizer.ClipGradientByValue
import org.jetbrains.kotlinx.dl.impl.summary.logSummary
import java.io.File

fun main() {
    /** Data **/
    val spectrogramData = ProcessingUtils.DIR_CREMA_D
    val epochs = 10
    val batchSize = 32
    val outputModelFilename = "DL_8c_3d_10e_32b"

    /** Train program **/
    println("I:\tStart program.")

    println("I:\tLoad and split dataset.")
    val (data, dim) = IOManager.loadDataset(
        dir = spectrogramData,
        shuffle = true
    ) { dim ->
        DatasetJvmPreprocessor.getPreprocessingPipeline(dim)
    }
    val (usable, _) = data.split(1.0)       /* take 100% of samples */
    val (train, valid) = usable.split(0.85) /* 85% samples is training set, 20% of samples is validation set */


    println("R:\tTraining data size: ${train.xSize()}, validation data size: ${valid.xSize()}, full data size: ${data.xSize()}.")

    println("I:\tBuild CNN for spectrogram data.")
    val network = CNNDeep.build(dim)
    network.use {
        it.compile(
            optimizer = Adam(
                learningRate = 0.001f,
                clipGradient = ClipGradientByValue(0.1f)
            ),
            loss = Losses.MSE,
            metric = Metrics.ACCURACY
        )

        println("I:\tFit CNN.")
        val earlyStopping = EarlyStopping(
            monitor = EpochTrainingEvent::valLossValue,
            minDelta = 0.0,
            patience = 5,
            verbose = true,
            mode = EarlyStoppingMode.AUTO,
            baseline = 0.01,
            restoreBestWeights = true
        )

        it.fit(
            trainingDataset = train,
            validationDataset = valid,
            epochs = epochs,
            trainBatchSize = batchSize,
            validationBatchSize = batchSize,
            callback = if(ProcessingUtils.MODEL_EARLY_STOP) earlyStopping else Callback()
        )

        it.logSummary()

        println("I:\tEvaluate CNN - train dataset.")
        val result = it.evaluate(
            dataset = train,
            batchSize = batchSize
        ).metrics[Metrics.ACCURACY]
        println("R:\t$result accuracy")

        println("I:\tConfusion Matrix")
        println(ClassifierEvaluator.confusionMatrix(model = it, testData = data))

        println("I:\tEvaluate CNN - valid dataset.")
        val resulValid = it.evaluate(
            dataset = valid,
            batchSize = batchSize
        ).metrics[Metrics.ACCURACY]
        println("R:\t$resulValid accuracy")

        println("I:\tConfusion Matrix")
        println(ClassifierEvaluator.confusionMatrix(model = it, testData = data))

        println("I:\tSave model.")
        it.save(
            File(ProcessingUtils.DIR_MODEL_OUTPUT, outputModelFilename),
            writingMode = WritingMode.OVERRIDE,
            savingFormat = SavingFormat.TF_GRAPH_CUSTOM_VARIABLES
        )
        println("R:\tModel saved to directory ${ProcessingUtils.DIR_MODEL_OUTPUT}/$outputModelFilename")
    }

    println("I:\tEnd program.")
}