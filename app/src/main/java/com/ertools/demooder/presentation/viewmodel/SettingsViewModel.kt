package com.ertools.demooder.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.settings.SettingsStore
import com.ertools.demooder.utils.AppConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsStore: SettingsStore
): ViewModel() {

    private val _deviceDamping = MutableStateFlow(AppConstants.SETTINGS_DEFAULT_DEVICE_DAMPING)
    val deviceDamping: StateFlow<Double> = _deviceDamping

    private val _signalDetectionPeriod = MutableStateFlow(AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_SECONDS)
    val signalDetectionPeriod: StateFlow<Double> = _signalDetectionPeriod

    private val _enableNotifications = MutableStateFlow(value = AppConstants.SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS)
    val enableNotifications: StateFlow<Boolean> = _enableNotifications

    private val _angerDetectionTime = MutableStateFlow(AppConstants.SETTINGS_DEFAULT_ANGER_DETECTION_TIME)
    val angerDetectionTime: StateFlow<Double> = _angerDetectionTime

    init {
        viewModelScope.launch {
            settingsStore.deviceDamping.collect {
                _deviceDamping.value = it
            }
            settingsStore.signalDetectionPeriod.collect {
                _signalDetectionPeriod.value = it
            }
            settingsStore.angerDetectionTime.collect {
                _angerDetectionTime.value = it
            }
            settingsStore.enableNotifications.collect {
                _enableNotifications.value = it
            }
            Log.d("SettingsViewModel", "Data: $deviceDamping, $signalDetectionPeriod, $enableNotifications, $angerDetectionTime")
        }
    }

    fun saveDeviceDamping(value: Double) {
        viewModelScope.launch {
            settingsStore.saveDeviceDamping(value)
            _deviceDamping.value = value
        }
    }

    fun saveSignalDetectionPeriod(value: Double) {
        viewModelScope.launch {
            settingsStore.saveSignalDetectionPeriod(value)
            _signalDetectionPeriod.value = value
        }
    }

    fun saveEnableNotifications(value: Boolean) {
        viewModelScope.launch {
            settingsStore.saveEnableNotifications(value)
            _enableNotifications.value = value
        }
    }

    fun saveAngerDetectionTime(value: Double) {
        viewModelScope.launch {
            settingsStore.saveAngerDetectionTime(value)
            _angerDetectionTime.value = value
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            settingsStore.saveDeviceDamping(AppConstants.SETTINGS_DEFAULT_DEVICE_DAMPING)
            _deviceDamping.value = AppConstants.SETTINGS_DEFAULT_DEVICE_DAMPING
            _signalDetectionPeriod.value = AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_SECONDS
            _enableNotifications.value = AppConstants.SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS
            _angerDetectionTime.value = AppConstants.SETTINGS_DEFAULT_ANGER_DETECTION_TIME
        }
    }
}

class SettingsViewModelFactory(private val settingsStore: SettingsStore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}