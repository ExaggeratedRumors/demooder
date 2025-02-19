package com.ertools.model.operation

import com.ertools.processing.commons.LabelsExtraction
import com.ertools.processing.commons.ProcessingUtils
import org.jetbrains.kotlinx.dl.api.core.SavingFormat
import org.jetbrains.kotlinx.dl.api.core.Sequential
import org.jetbrains.kotlinx.dl.api.core.WritingMode
import org.jetbrains.kotlinx.dl.api.core.callback.Callback
import org.jetbrains.kotlinx.dl.api.core.callback.EarlyStopping
import org.jetbrains.kotlinx.dl.api.core.callback.EarlyStoppingMode
import org.jetbrains.kotlinx.dl.api.core.history.EpochTrainingEvent
import org.jetbrains.kotlinx.dl.api.core.loss.Losses
import org.jetbrains.kotlinx.dl.api.core.metric.Metrics
import org.jetbrains.kotlinx.dl.api.core.optimizer.Adam
import org.jetbrains.kotlinx.dl.api.core.optimizer.ClipGradientByValue
import org.jetbrains.kotlinx.dl.api.inference.InferenceModel
import org.jetbrains.kotlinx.dl.api.inference.TensorFlowInferenceModel
import org.jetbrains.kotlinx.dl.dataset.Dataset
import org.jetbrains.kotlinx.dl.dataset.evaluate
import java.io.File

fun Sequential.compile() {
    this.compile(
        optimizer = Adam(
            learningRate = 0.001f,
            clipGradient = ClipGradientByValue(0.1f)
        ),
        loss = Losses.MSE,
        metric = Metrics.ACCURACY
    )
}

fun Sequential.fit(
    train: Dataset,
    validation: Dataset,
    epochs: Int = ProcessingUtils.MODEL_EPOCHS,
    batchSize: Int = ProcessingUtils.MODEL_BATCH_SIZE,
    earlyStop: Boolean = ProcessingUtils.MODEL_EARLY_STOP
) {
    val earlyStopping = EarlyStopping(
        monitor = EpochTrainingEvent::valLossValue,
        minDelta = 0.0,
        patience = 5,
        verbose = true,
        mode = EarlyStoppingMode.AUTO,
        baseline = 0.01,
        restoreBestWeights = true
    )

    this.fit(
        trainingDataset = train,
        validationDataset = validation,
        epochs = epochs,
        trainBatchSize = batchSize,
        validationBatchSize = batchSize,
        callback = if(earlyStop) earlyStopping else Callback()
    )
}

fun Sequential.evaluate(
    test: Dataset,
    batchSize: Int = ProcessingUtils.MODEL_BATCH_SIZE
): Double? {
    return this.evaluate(
        dataset = test,
        batchSize = batchSize
    ).metrics[Metrics.ACCURACY]
}


fun InferenceModel.confusionMatrix(testData: Dataset) {
    val classes = LabelsExtraction.Emotion.entries
    val confusionMatrix = Array(classes.size) { IntArray(classes.size) }
    for(i in 0 until testData.xSize()) {
        val predictedLabel = this.predict(testData.getX(i))
        val trueLabel = testData.getY(i).toInt()
        confusionMatrix[trueLabel][predictedLabel] += 1
    }
    confusionMatrix.forEachIndexed { i, row ->
        println("${LabelsExtraction.Emotion.entries[i]}\t|\t${row.joinToString("\t")}")
    }
    val labels = (0 until confusionMatrix[0].size).map {
        LabelsExtraction.Emotion.entries[it]
    }.joinToString("\t")
    println("\t\t$labels")
}

fun Sequential.save(
    modelName: String = ProcessingUtils.MODEL_DEFAULT_NAME
) {
    this.save(File(ProcessingUtils.DIR_MODEL_OUTPUT, modelName), writingMode = WritingMode.OVERRIDE, savingFormat = SavingFormat.TF_GRAPH)
}

fun Sequential.load(
    path: String = ProcessingUtils.DIR_MODEL_OUTPUT,
    modelName: String = ProcessingUtils.MODEL_DEFAULT_NAME
) {
    this.loadWeights(File(path, modelName))
}