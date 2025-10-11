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

/**
 * VieModel for calculating and providing statistics based on predictions.
 */
class StatisticsViewModel(
    application: Application
): AndroidViewModel(application), PredictionProvider {
    private val predictionRepository: PredictionRepository = PredictionRepository
    private val predictionHistory: StateFlow<List<Prediction>> = predictionRepository.predictionHistory

    /********************/
    /** Implementation **/
    /********************/

    override fun last(amount: Int): StateFlow<List<Prediction>> {
        return predictionHistory
            .map { it.takeLast(amount)}
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                emptyList()
            )
    }

    override fun count(emotion: Emotion): StateFlow<Int> {
        return predictionHistory
            .map { predictions ->
                predictions.count { it.label == emotion }
            }.stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                0
            )
    }

    override fun reset() {
        predictionRepository.reset()
    }
}
