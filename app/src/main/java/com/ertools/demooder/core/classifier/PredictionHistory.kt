package com.ertools.demooder.core.classifier

import com.ertools.demooder.utils.AppConstants.HISTORY_MAX_SIZE
import com.ertools.processing.commons.Emotion

object PredictionHistory {
    private val _predictionHistory = mutableListOf<Prediction>()
    val predictionHistory: List<Prediction>
        get() = _predictionHistory

    fun updatePredictions(newPredictions: List<Prediction>) {
        val prediction = newPredictions.maxBy { it.confidence }
        if(_predictionHistory.size == HISTORY_MAX_SIZE)
            _predictionHistory.removeFirst()
        _predictionHistory.add(prediction)
    }

    val lastTwoPredictions: List<Prediction>
        get() = _predictionHistory.takeLast(2)

    val angryPredictionAmount
        get() = _predictionHistory.count { it.label == Emotion.ANG }

    fun count(prediction: Prediction) {
        _predictionHistory.add(prediction)
    }

    fun reset() {
        _predictionHistory.clear()
    }
}