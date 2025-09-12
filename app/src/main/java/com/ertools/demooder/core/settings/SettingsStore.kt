package com.ertools.demooder.core.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.ertools.demooder.utils.AppConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SettingsStore(private val context: Context) {
    companion object {
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore(
            name = SettingsPreferences.PREFERENCES_NAME
        )
    }

    val deviceDamping: Flow<Double> = context.datastore.data.map {
        it[SettingsPreferences.DEVICE_DAMPING] ?: AppConstants.SETTINGS_DEFAULT_DEVICE_DAMPING
    }

    val signalDetectionPeriod: Flow<Double> = context.datastore.data.map {
        it[SettingsPreferences.SIGNAL_DETECTION_PERIOD] ?: AppConstants.SETTINGS_DEFAULT_SIGNAL_DETECTION_SECONDS
    }

    val enableNotifications: Flow<Boolean> = context.datastore.data.map {
        it[SettingsPreferences.ENABLE_NOTIFICATIONS] ?: AppConstants.SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS
    }

    val angerDetectionTime: Flow<Double> = context.datastore.data.map {
        it[SettingsPreferences.ANGER_DETECTION_TIME] ?: AppConstants.SETTINGS_DEFAULT_ANGER_DETECTION_TIME
    }

    val phoneNumber: Flow<String> = context.datastore.data.map {
        it[SettingsPreferences.PHONE_NUMBER] ?: AppConstants.SETTINGS_DEFAULT_PHONE_NUMBER
    }

    suspend fun saveDeviceDamping(value: Double) {
        context.datastore.edit { preferences ->
            preferences[SettingsPreferences.DEVICE_DAMPING] = value
        }
    }

    suspend fun saveSignalDetectionPeriod(value: Double) {
        context.datastore.edit { preferences ->
            preferences[SettingsPreferences.SIGNAL_DETECTION_PERIOD] = value
        }
    }

    suspend fun saveEnableNotifications(value: Boolean) {
        context.datastore.edit { preferences ->
            preferences[SettingsPreferences.ENABLE_NOTIFICATIONS] = value
        }
    }

    suspend fun saveAngerDetectionTime(value: Double) {
        context.datastore.edit { preferences ->
            preferences[SettingsPreferences.ANGER_DETECTION_TIME] = value
        }
    }

    suspend fun savePhoneNumber(value: String) {
        context.datastore.edit { preferences ->
            preferences[SettingsPreferences.PHONE_NUMBER] = value
        }
    }
}