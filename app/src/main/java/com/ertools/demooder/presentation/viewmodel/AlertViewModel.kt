package com.ertools.demooder.presentation.viewmodel

import android.telephony.SmsManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.classifier.PredictionRepository
import com.ertools.demooder.utils.AppConstants.NOTIFICATION_SMS_CONTENT
import com.ertools.demooder.utils.AppConstants.SETTINGS_DEFAULT_PHONE_NUMBER
import com.ertools.processing.commons.Emotion
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlertViewModel(
    val maxAngerDetectionTimeSeconds: Int,
    val analyzePeriodSeconds: Int,
): ViewModel() {
    val angerDataFlow = PredictionRepository.predictionHistory.map {
        it.count { prediction -> prediction.label == Emotion.ANG }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        0
    )

    val alertHasBeenSent = mutableStateOf(false)

    fun startListening() {
        viewModelScope.launch {
            while(!alertHasBeenSent.value) {
                if(angerDataFlow.value * analyzePeriodSeconds >= maxAngerDetectionTimeSeconds) {
                    sendSms()
                    alertHasBeenSent.value = true
                }
            }
        }
    }

    private fun sendSms() {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(
                SETTINGS_DEFAULT_PHONE_NUMBER,
                null,
                NOTIFICATION_SMS_CONTENT,
                null,
                null
            )
            Log.d("SMS", "SMS sent successfully to $NOTIFICATION_SMS_CONTENT")
        } catch (e: Exception) {
            Log.e("SMS", "Failed to send SMS: ${e.message}")
        }
    }
}

class AlertViewModelFactory(
    val maxAngerDetectionTimeSeconds: Int,
    val analyzePeriodSeconds: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlertViewModel(
                maxAngerDetectionTimeSeconds,
                analyzePeriodSeconds
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}