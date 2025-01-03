package com.ertools.demooder.core.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object SettingsPreferences {
    const val PREFERENCES_NAME = "settings"

    val DEVICE_DAMPING = intPreferencesKey("device_damping")
    val SIGNAL_DETECTION_PERIOD = intPreferencesKey("signal_detection_period")
    val ENABLE_NOTIFICATIONS = booleanPreferencesKey("enable_notifications")
    val ANGER_DETECTION_TIME = intPreferencesKey("anger_detection_time")
}