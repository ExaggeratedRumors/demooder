package com.ertools.demooder.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.R
import com.ertools.demooder.core.audio.AudioProvider
import com.ertools.demooder.core.classifier.Prediction
import com.ertools.demooder.core.classifier.PredictionRepository
import com.ertools.demooder.core.notifications.MediaService
import com.ertools.demooder.core.notifications.NotificationAction
import com.ertools.demooder.core.notifications.NotificationData
import com.ertools.demooder.core.notifications.NotificationEventStream
import com.ertools.demooder.utils.AppConstants
import com.ertools.demooder.utils.Permissions
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val audioProvider: AudioProvider,
    predictionRepository: PredictionRepository
): ViewModel() {

    /************/
    /** Data flows **/
    /************/

    private var _serviceRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val serviceRunning: StateFlow<Boolean> = _serviceRunning.asStateFlow()

    private val _errors: MutableSharedFlow<String> = MutableSharedFlow()
    val notificationErrors = _errors.asSharedFlow()

    private val lastTwoPredictions: StateFlow<List<Prediction>> = predictionRepository.predictionHistory
        .map { it.takeLast(2)}
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    /********************/
    /** Public methods **/
    /********************/

    fun runTasks(context: Context) {
        startListeningTask(context)
        startEmittingTask(context)
        startPredictionTask(context)
    }

    /*********************/
    /** Private methods **/
    /*********************/

    private fun startListeningTask(context: Context) {
        viewModelScope.launch {
            startBackgroundTask(context, AppConstants.NOTIFICATION_ACTION_NOTIFY)
            NotificationEventStream.events.collect { notificationData ->
                when(notificationData.action) {
                    NotificationAction.START ->
                        audioProvider.start()
                    NotificationAction.STOP ->
                        audioProvider.stop()
                    else -> {}
                }
            }
        }
    }

    private fun startEmittingTask(context: Context) {
        viewModelScope.launch {
            audioProvider.isRunning().collect { running ->
                val notificationData = if (running) NotificationData(
                    action = NotificationAction.START,
                    title = context.getString(R.string.prediction_result_label),
                    subtitle = context.getString(R.string.prediction_result_loading)
                ) else NotificationData(
                    action = NotificationAction.STOP,
                    title = context.getString(R.string.prediction_placeholder)
                )
                updateBackgroundTask(notificationData)
            }
        }
    }

    private fun startPredictionTask(context: Context) {
        viewModelScope.launch {
            lastTwoPredictions.collect { it ->
                val currentPrediction = it.firstOrNull()?.label?.name ?: context.getString(R.string.prediction_result_loading)
                val previousPrediction = it.getOrNull(1)?.label?.name ?: context.getString(R.string.empty)

                val notificationData = NotificationData(
                    action = NotificationAction.UPDATE,
                    title = "Prediction: $currentPrediction",
                    subtitle = "Previous: $previousPrediction"
                )
                updateBackgroundTask(notificationData)

            }
        }
    }

    /**
     * Set background task for audio service.
     */
    private suspend fun startBackgroundTask(context: Context, action: String) {
        if(!Permissions.isPostNotificationPermissionGained(context)) {
            _errors.emit(context.getString(R.string.error_notification_permission))
            return
        }
        val notificationData = NotificationData(
            action = NotificationAction.INIT,
            title = context.getString(R.string.prediction_placeholder),
            subtitle = context.getString(R.string.empty)
        )

        val serviceIntent = Intent(context, MediaService::class.java).apply {
            this.action = action
            this.putExtra(AppConstants.NOTIFICATION_DATA, notificationData)
        }
        Log.d("NotificationViewModel", "Starting service with action: $action")
        ContextCompat.startForegroundService(context, serviceIntent)
        _serviceRunning.value = true
    }

    private fun updateBackgroundTask(notificationData: NotificationData) {
        if(!serviceRunning.value) return
        NotificationEventStream.events.tryEmit(notificationData)
    }

    /**
     * Stop background task for audio service.
     */
    private fun stopBackgroundTask() {
        if(!serviceRunning.value) return
        val notificationData = NotificationData(action = NotificationAction.DESTROY)
        NotificationEventStream.events.tryEmit(notificationData)
    }

    /*********************/
    /** Implementations **/
    /*********************/
    override fun onCleared() {
        super.onCleared()
        stopBackgroundTask()
    }
}

class NotificationViewModelFactory(
    val audioProvider: AudioProvider,
    val predictionRepository: PredictionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel(
                audioProvider, predictionRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}