package com.ertools.demooder.core.host

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import com.ertools.demooder.core.notifications.NotificationBuilder

class DemooderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        NotificationBuilder.buildChannels(notificationManager)
    }
}