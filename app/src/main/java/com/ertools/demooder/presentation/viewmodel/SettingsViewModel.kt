package com.ertools.demooder.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.data.SettingsPreferences
import com.ertools.demooder.utils.AppConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private val Application.datastore by preferencesDataStore(
    name = SettingsPreferences.PREFERENCES_NAME
)

class  SettingsViewModel(application: Application): AndroidViewModel(application) {
    private val dataStore = application.datastore

    private val _deviceDamping = MutableStateFlow(AppConstants.SETTINGS_DEFAULT_DEVICE_DAMPING)
    val deviceDamping: StateFlow<Double> = _deviceDamping

    private val _signalDetectionPeriod = MutableStateFlow(AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_PERIOD)
    val signalDetectionPeriod: StateFlow<Double> = _signalDetectionPeriod

    private val _enableNotifications = MutableStateFlow(value = AppConstants.SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS)
    val enableNotifications: StateFlow<Boolean> = _enableNotifications

    private val _angerDetectionTime = MutableStateFlow(AppConstants.SETTINGS_DEFAULT_ANGER_DETECTION_TIME)
    val angerDetectionTime: StateFlow<Double> = _angerDetectionTime

    init {
        Log.d("SettingsViewModel", "init")
        viewModelScope.launch {
            dataStore.data.collect { preferences ->
                _deviceDamping.value = preferences[SettingsPreferences.DEVICE_DAMPING]
                    ?: AppConstants.SETTINGS_DEFAULT_DEVICE_DAMPING
                _signalDetectionPeriod.value = preferences[SettingsPreferences.SIGNAL_DETECTION_PERIOD]
                    ?: AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_PERIOD
                _enableNotifications.value = preferences[SettingsPreferences.ENABLE_NOTIFICATIONS]
                    ?: AppConstants.SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS
                _angerDetectionTime.value = preferences[SettingsPreferences.ANGER_DETECTION_TIME]
                    ?: AppConstants.SETTINGS_DEFAULT_ANGER_DETECTION_TIME
                Log.d("SettingsViewModel", "pref: ${preferences[SettingsPreferences.ENABLE_NOTIFICATIONS]}")
                Log.d("SettingsViewModel", "state: ${_enableNotifications.value}")

            }
        }
    }

    fun saveDeviceDamping(value: Double) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[SettingsPreferences.DEVICE_DAMPING] = value
            }
            _deviceDamping.value = value
        }
    }

    fun saveSignalDetectionPeriod(value: Double) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[SettingsPreferences.SIGNAL_DETECTION_PERIOD] = value
            }
            _signalDetectionPeriod.value = value
        }
    }

    fun saveEnableNotifications(value: Boolean) {
        Log.d("SettingsViewModel", "111: $value")
        viewModelScope.launch {
            dataStore.edit { preferences ->
                Log.d("SettingsViewModel", "222: $value")
                preferences[SettingsPreferences.ENABLE_NOTIFICATIONS] = value
            }
            Log.d("SettingsViewModel", "333: $value")
            _enableNotifications.value = value
        }
    }

    fun saveAngerDetectionTime(value: Double) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[SettingsPreferences.ANGER_DETECTION_TIME] = value
            }
            _angerDetectionTime.value = value
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences.clear()
            }
            _deviceDamping.value = AppConstants.SETTINGS_DEFAULT_DEVICE_DAMPING
            _signalDetectionPeriod.value = AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_PERIOD
            _enableNotifications.value = AppConstants.SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS
            _angerDetectionTime.value = AppConstants.SETTINGS_DEFAULT_ANGER_DETECTION_TIME
        }
    }
}