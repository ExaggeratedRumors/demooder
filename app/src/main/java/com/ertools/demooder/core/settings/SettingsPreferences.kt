package com.ertools.demooder.core.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey

object SettingsPreferences {
    const val PREFERENCES_NAME = "settings"

    val DEVICE_DAMPING = doublePreferencesKey("device_damping")
    val SIGNAL_DETECTION_PERIOD = doublePreferencesKey("signal_detection_period")
    val ENABLE_NOTIFICATIONS = booleanPreferencesKey("enable_notifications")
    val ANGER_DETECTION_TIME = doublePreferencesKey("anger_detection_time")
}