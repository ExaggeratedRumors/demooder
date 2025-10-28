package com.ertools.demooder.presentation.viewmodel

import android.telephony.SmsManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.classifier.PredictionRepository
import com.ertools.demooder.utils.AppConstants
import com.ertools.demooder.utils.AppConstants.NOTIFICATION_SMS_CONTENT
import com.ertools.demooder.utils.AppConstants.SETTINGS_DEFAULT_PHONE_NUMBER
import com.ertools.processing.commons.Emotion
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlertViewModel(
    maxAngerDetectionTimeSecondsFlow: Flow<Double>,
    analyzePeriodSecondsFlow: Flow<Double>,
    phoneNumber: Flow<String>
): ViewModel() {
    /** Data flows **/
    val phoneNumber = phoneNumber.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        SETTINGS_DEFAULT_PHONE_NUMBER
    )
    private val maxAngerDetectionTime = maxAngerDetectionTimeSecondsFlow.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppConstants.SETTINGS_DEFAULT_ANGER_DETECTION_TIME
    )
    private val analyzePeriodSeconds = analyzePeriodSecondsFlow.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_SECONDS
    )
    private val angerDataFlow = PredictionRepository.predictionHistory.map {
        it.count { prediction -> prediction.label == Emotion.ANG }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        0
    )
    private val alertHasBeenSent = mutableStateOf(false)

    /*********/
    /** API **/
    /*********/
    fun startListening() {
        viewModelScope.launch {
            while(!alertHasBeenSent.value) {
                if(angerDataFlow.value * analyzePeriodSeconds.value >= maxAngerDetectionTime.value) {
                    sendSms()
                    alertHasBeenSent.value = true
                }
                delay(analyzePeriodSeconds.value.toLong())
            }
        }
    }


    /*************/
    /** Private **/
    /*************/
    private fun sendSms() {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(
                SETTINGS_DEFAULT_PHONE_NUMBER,
                null,
                phoneNumber.value,
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
    val maxAngerDetectionTimeSeconds: Flow<Double>,
    val analyzePeriodSeconds: Flow<Double>,
    val phoneNumber: Flow<String>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlertViewModel(
                maxAngerDetectionTimeSeconds,
                analyzePeriodSeconds,
                phoneNumber
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}