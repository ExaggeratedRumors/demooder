package com.ertools.demooder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.settings.SettingsStore
import com.ertools.demooder.utils.AppConstants
import com.ertools.demooder.utils.AppFormat
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for managing settings in the application.
 * It interacts with the SettingsStore to retrieve and save settings values.
 */
class SettingsViewModel(
    private val settingsStore: SettingsStore
) : ViewModel() {
    /** Device damping factor for signal processing **/
    val deviceDamping: StateFlow<String> = settingsStore.deviceDamping.map {
        AppFormat.doubleToOnePrecString(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppFormat.doubleToOnePrecString(AppConstants.SETTINGS_DEFAULT_DEVICE_DAMPING)
    )

    /** Signal detection period in seconds **/
    val signalDetectionPeriod: StateFlow<Double> = settingsStore.signalDetectionPeriod
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_SECONDS
        )

    val signalDetectionPeriodAsString: StateFlow<String> = settingsStore.signalDetectionPeriod.map {
        AppFormat.doubleToOnePrecString(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppFormat.doubleToOnePrecString(AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_SECONDS)
    )

    /** Enable or disable notifications **/
    val enableNotifications: StateFlow<Boolean> = settingsStore.enableNotifications
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AppConstants.SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS
        )

    /** Anger detection time in seconds **/
    val angerDetectionTime: StateFlow<String> = settingsStore.angerDetectionTime.map {
        AppFormat.doubleToOnePrecString(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppFormat.doubleToOnePrecString(AppConstants.SETTINGS_DEFAULT_ANGER_DETECTION_TIME)
    )

    /** Phone number for notifications **/
    val phoneNumber: StateFlow<String> = settingsStore.phoneNumber
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AppConstants.SETTINGS_DEFAULT_PHONE_NUMBER
        )

    /**********/
    /** API  **/
    /**********/

    fun saveDeviceDamping(value: Double) {
        viewModelScope.launch {
            settingsStore.saveDeviceDamping(value)
        }
    }

    fun saveSignalDetectionPeriod(value: Double) {
        viewModelScope.launch {
            settingsStore.saveSignalDetectionPeriod(value)
        }
    }

    fun saveEnableNotifications(value: Boolean) {
        viewModelScope.launch {
            settingsStore.saveEnableNotifications(value)
        }
    }

    fun saveAngerDetectionTime(value: Double) {
        viewModelScope.launch {
            settingsStore.saveAngerDetectionTime(value)
        }
    }

    fun savePhoneNumber(value: String) {
        viewModelScope.launch {
            settingsStore.savePhoneNumber(value)
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            settingsStore.saveDeviceDamping(AppConstants.SETTINGS_DEFAULT_DEVICE_DAMPING)
            settingsStore.saveSignalDetectionPeriod(AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_SECONDS)
            settingsStore.saveEnableNotifications(AppConstants.SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS)
            settingsStore.saveAngerDetectionTime(AppConstants.SETTINGS_DEFAULT_ANGER_DETECTION_TIME)
            settingsStore.savePhoneNumber(AppConstants.SETTINGS_DEFAULT_PHONE_NUMBER)
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