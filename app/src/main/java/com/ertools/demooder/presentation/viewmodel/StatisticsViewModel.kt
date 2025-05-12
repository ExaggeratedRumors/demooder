package com.ertools.demooder.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ertools.demooder.core.classifier.Prediction
import com.ertools.processing.commons.Emotion
import kotlinx.coroutines.flow.MutableStateFlow

class StatisticsViewModel(application: Application): AndroidViewModel(application ) {
    private val _predictionsHistory = MutableStateFlow(mutableListOf<Prediction>())



    val lastTwoPredictions: List<Prediction>
        get() = _predictionsHistory.value.takeLast(2)

    val angryPredictionAmount
        get() = _predictionsHistory.value.count { it.label == Emotion.ANG }

    fun count(prediction: Prediction) {
        _predictionsHistory.value.add(prediction)
    }

    fun reset() {
        _predictionsHistory.value.clear()
    }
}