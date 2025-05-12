package com.ertools.demooder.core.settings

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore

val Application.datastore by preferencesDataStore(
    name = SettingsPreferences.PREFERENCES_NAME
)