package com.ertools.demooder.presentation.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.data.SettingsPreferences
import com.ertools.demooder.utils.SETTINGS_DEFAULT_ANGER_DETECTION_TIME
import com.ertools.demooder.utils.SETTINGS_DEFAULT_DEVICE_DAMPING
import com.ertools.demooder.utils.SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS
import com.ertools.demooder.utils.SETTINGS_DEFAULT_SIGNAL_DETECTION_PERIOD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Application.datastore by preferencesDataStore(
    name = SettingsPreferences.PREFERENCES_NAME
)

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    private val dataStore = application.datastore

    val deviceDamping: Flow<Double> = dataStore.data.map { preferences ->
        preferences[SettingsPreferences.DEVICE_DAMPING] ?: SETTINGS_DEFAULT_DEVICE_DAMPING
    }

    val signalDetectionPeriod: Flow<Double> = dataStore.data.map { preferences ->
        preferences[SettingsPreferences.SIGNAL_DETECTION_PERIOD] ?: SETTINGS_DEFAULT_SIGNAL_DETECTION_PERIOD
    }

    val enableNotifications: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SettingsPreferences.ENABLE_NOTIFICATIONS] ?: SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS
    }

    val angerDetectionTime: Flow<Double> = dataStore.data.map { preferences ->
        preferences[SettingsPreferences.ANGER_DETECTION_TIME] ?: SETTINGS_DEFAULT_ANGER_DETECTION_TIME
    }

    fun saveDeviceDamping(value: Double) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[SettingsPreferences.DEVICE_DAMPING] = value
            }
        }
    }

    fun saveSignalDetectionPeriod(value: Double) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[SettingsPreferences.SIGNAL_DETECTION_PERIOD] = value
            }
        }
    }

    fun saveEnableNotifications(value: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[SettingsPreferences.ENABLE_NOTIFICATIONS] = value
            }
        }
    }

    fun saveAngerDetectionTime(value: Double) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[SettingsPreferences.ANGER_DETECTION_TIME] = value
            }
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }
}