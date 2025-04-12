package com.ertools.demooder.core.classifier

import kotlinx.coroutines.flow.StateFlow


interface PredictionProvider {
    fun getPrediction(): StateFlow<List<Pair<String, Float>>>
}