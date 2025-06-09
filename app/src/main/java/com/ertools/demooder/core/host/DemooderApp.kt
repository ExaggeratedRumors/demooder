package com.ertools.demooder.core.host

import android.app.Application
import android.app.NotificationManager
import com.ertools.demooder.core.notifications.NotificationBuilder

class DemooderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val notificationManager = getSystemService(NotificationManager::class.java)
        NotificationBuilder.buildChannels(notificationManager)
    }
}