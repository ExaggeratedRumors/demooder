package com.ertools.model.evaluation

import com.ertools.processing.commons.LabelsExtraction
import org.jetbrains.kotlinx.dl.api.inference.InferenceModel
import org.jetbrains.kotlinx.dl.dataset.Dataset

object ClassifierEvaluator {
    fun confusionMatrix(model: InferenceModel, testData: Dataset): String {
        val classes = LabelsExtraction.Emotion.entries
        val confusionMatrix = Array(classes.size) { IntArray(classes.size) }
        for(i in 0 until testData.xSize()) {
            val predictedLabel = model.predict(testData.getX(i))
            val trueLabel = testData.getY(i).toInt()
            confusionMatrix[trueLabel][predictedLabel] += 1
        }

        val result = sequence {
            confusionMatrix.forEachIndexed { i, row ->
                yield("${LabelsExtraction.Emotion.entries[i]}\t|\t${row.joinToString("\t")}")
            }

            val labels = (0 until confusionMatrix[0].size).map {
                LabelsExtraction.Emotion.entries[it]
            }.joinToString("\t")
            yield("\t\t$labels")
        }

        return result.joinToString("\n")
    }
}
