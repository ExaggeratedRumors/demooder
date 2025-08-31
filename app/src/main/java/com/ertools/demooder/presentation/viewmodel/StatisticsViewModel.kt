package com.ertools.demooder.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.classifier.Prediction
import com.ertools.demooder.core.classifier.PredictionProvider
import com.ertools.demooder.core.classifier.PredictionRepository
import com.ertools.processing.commons.Emotion
import kotlinx.coroutines.flow.MutableStateFlow
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

    private val _emotionsTime: Map<Emotion, StateFlow<Long>> = Emotion.entries.toTypedArray()
        .associateWith { emotion ->
            predictionHistory.map { history ->
                history.filter { it.label == emotion }.sumOf { 1L }
            }.stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                0L
            )
        }

    private val _isDescriptionVisible = MutableStateFlow(false)
    val isDescriptionVisible: StateFlow<Boolean> = _isDescriptionVisible

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

    override fun count(label: Emotion): StateFlow<Int> {
        return predictionHistory.map { history ->
            history.count { it.label == label }
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            0
        )
    }

    fun getEmotionFlow(emotion: Emotion): StateFlow<Long> {
        return _emotionsTime[emotion] ?: MutableStateFlow(0L)
    }

    override fun reset() {
        predictionRepository.reset()
    }
}
