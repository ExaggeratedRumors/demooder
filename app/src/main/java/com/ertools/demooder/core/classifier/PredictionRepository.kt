package com.ertools.demooder.core.classifier

import com.ertools.demooder.utils.AppConstants.HISTORY_MAX_SIZE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PredictionRepository {
    private val _predictionHistory = MutableStateFlow<List<Prediction>>(emptyList())
    val predictionHistory: StateFlow<List<Prediction>>
        get() = _predictionHistory

    fun updatePredictions(newPredictions: List<Prediction>) {
        val prediction = newPredictions.maxBy { it.confidence }
        val currentHistory = _predictionHistory.value.toMutableList()
        if(currentHistory.size == HISTORY_MAX_SIZE)  currentHistory.removeAt(0)
        currentHistory.add(prediction)
        _predictionHistory.value = currentHistory.toList()
    }

    fun reset() {
        _predictionHistory.value = emptyList()
    }
}
