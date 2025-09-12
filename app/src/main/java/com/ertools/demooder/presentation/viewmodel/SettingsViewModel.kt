package com.ertools.demooder.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.settings.SettingsStore
import com.ertools.demooder.utils.AppConstants
import com.ertools.demooder.utils.AppFormat
import kotlinx.coroutines.flow.MutableStateFlow
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
): ViewModel() {
    /** Device damping factor for signal processing **/
    private val _deviceDamping = MutableStateFlow(AppConstants.SETTINGS_DEFAULT_DEVICE_DAMPING)
    val deviceDamping: StateFlow<Double> = _deviceDamping
    val deviceDampingAsString: StateFlow<String> = _deviceDamping.map {
        AppFormat.doubleToString(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppFormat.doubleToString(AppConstants.SETTINGS_DEFAULT_DEVICE_DAMPING)
    )

    /** Signal detection period in seconds **/
    private val _signalDetectionPeriod = MutableStateFlow(AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_SECONDS)
    val signalDetectionPeriod: StateFlow<Double> = _signalDetectionPeriod
    val signalDetectionPeriodAsString: StateFlow<String> = _signalDetectionPeriod.map {
        AppFormat.doubleToString(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppFormat.doubleToString(AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_SECONDS)
    )

    /** Enable or disable notifications **/
    private val _enableNotifications = MutableStateFlow(value = AppConstants.SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS)
    val enableNotifications: StateFlow<Boolean> = _enableNotifications

    private val _angerDetectionTime = MutableStateFlow(AppConstants.SETTINGS_DEFAULT_ANGER_DETECTION_TIME)

    /** Anger detection time in seconds **/
    val angerDetectionTime: StateFlow<Double> = _angerDetectionTime
    val angerDetectionTimeAsString: StateFlow<String> = _angerDetectionTime.map {
        AppFormat.doubleToString(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppFormat.doubleToString(AppConstants.SETTINGS_DEFAULT_ANGER_DETECTION_TIME)
    )

    /** Phone number for notifications **/
    private val _phoneNumber = MutableStateFlow(AppConstants.SETTINGS_DEFAULT_PHONE_NUMBER)
    val phoneNumber: StateFlow<String> = _phoneNumber

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
            settingsStore.phoneNumber.collect {
                _phoneNumber.value = it
            }
            Log.d("SettingsViewModel", "Data: $deviceDamping, $signalDetectionPeriod, $enableNotifications, $angerDetectionTime, $phoneNumber")
        }
    }


    /**********/
    /** API  **/
    /**********/

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

    fun savePhoneNumber(value: String) {
        viewModelScope.launch {
            settingsStore.savePhoneNumber(value)
            _phoneNumber.value = value
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            settingsStore.saveDeviceDamping(AppConstants.SETTINGS_DEFAULT_DEVICE_DAMPING)
            _deviceDamping.value = AppConstants.SETTINGS_DEFAULT_DEVICE_DAMPING
            _signalDetectionPeriod.value = AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_SECONDS
            _enableNotifications.value = AppConstants.SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS
            _angerDetectionTime.value = AppConstants.SETTINGS_DEFAULT_ANGER_DETECTION_TIME
            _phoneNumber.value = AppConstants.SETTINGS_DEFAULT_PHONE_NUMBER
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