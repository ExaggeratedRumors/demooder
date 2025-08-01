package com.ertools.demooder.core.host

import android.app.Application
import com.ertools.demooder.core.notifications.NotificationBuilder

class DemooderApp : Application() {
    override fun onCreate() {
        super.onCreate()

        NotificationBuilder.buildChannels(this)
    }
}