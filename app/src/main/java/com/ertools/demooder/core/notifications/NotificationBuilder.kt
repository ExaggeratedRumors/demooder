package com.ertools.demooder.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import com.ertools.demooder.utils.AppConstants

object NotificationBuilder{
    fun buildChannels(manager: NotificationManager) {
        val microphoneChannel = NotificationChannel(
            AppConstants.NOTIFICATION_MICROPHONE_CHANNEL_ID,
            AppConstants.NOTIFICATION_MICROPHONE_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )

        val mediaChannel = NotificationChannel(
            AppConstants.NOTIFICATION_MEDIA_CHANNEL_ID,
            AppConstants.NOTIFICATION_MEDIA_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )

        manager.createNotificationChannels(
            listOf(microphoneChannel, mediaChannel)
        )
    }
}