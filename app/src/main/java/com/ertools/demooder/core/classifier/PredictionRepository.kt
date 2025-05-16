package com.ertools.demooder.core.classifier

import com.ertools.demooder.utils.AppConstants.HISTORY_MAX_SIZE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PredictionRepository {
    private val _predictionHistory = MutableStateFlow<MutableList<Prediction>>(mutableListOf())
    val predictionHistory: StateFlow<List<Prediction>>
        get() = _predictionHistory

    fun updatePredictions(newPredictions: List<Prediction>) {
        val prediction = newPredictions.maxBy { it.confidence }
        if(_predictionHistory.value.size == HISTORY_MAX_SIZE)
            _predictionHistory.value.removeAt(0)
        _predictionHistory.value.add(prediction)
    }

    fun reset() {
        _predictionHistory.value.clear()
    }
}