package com.ertools.demooder.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.classifier.Prediction
import com.ertools.demooder.core.classifier.PredictionProvider
import com.ertools.demooder.core.classifier.PredictionRepository
import com.ertools.processing.commons.Emotion
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take

class StatisticsViewModel(
    application: Application
): AndroidViewModel(application), PredictionProvider {
    private val predictionRepository: PredictionRepository = PredictionRepository
    private val predictionHistory: StateFlow<List<Prediction>> = predictionRepository.predictionHistory

    /********************/
    /** Implementation **/
    /********************/
    override fun last(amount: Int): StateFlow<List<Prediction>> {
        return predictionHistory.take(amount).stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )
    }

    override fun proportion(label: Emotion): StateFlow<Float> {
        return predictionHistory.map { history ->
            val count = history.count { it.label == label }
            val total = history.size
            1f * count / total
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            0f
        )
    }

    override fun reset() {
        predictionRepository.reset()
    }
}