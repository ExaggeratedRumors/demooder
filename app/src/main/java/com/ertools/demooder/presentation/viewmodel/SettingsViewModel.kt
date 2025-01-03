package com.ertools.demooder.presentation.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ertools.demooder.core.data.SettingsPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Application.datastore by preferencesDataStore(
    name = SettingsPreferences.PREFERENCES_NAME
)

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    private val dataStore = application.datastore

    val deviceDamping: Flow<Int> = dataStore.data.map { preferences ->
        preferences[SettingsPreferences.DEVICE_DAMPING] ?: 0
    }

    val signalDetectionPeriod: Flow<Int> = dataStore.data.map { preferences ->
        preferences[SettingsPreferences.SIGNAL_DETECTION_PERIOD] ?: 0
    }

    val enableNotifications: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SettingsPreferences.ENABLE_NOTIFICATIONS] ?: true
    }

    val angerDetectionTime: Flow<Int> = dataStore.data.map { preferences ->
        preferences[SettingsPreferences.ANGER_DETECTION_TIME] ?: 0
    }

    fun saveDeviceDamping(value: Int) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[SettingsPreferences.DEVICE_DAMPING] = value
            }
        }
    }

    fun saveSignalDetectionPeriod(value: Int) {
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

    fun saveAngerDetectionTime(value: Int) {
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