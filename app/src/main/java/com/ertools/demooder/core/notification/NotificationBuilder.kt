package com.ertools.demooder.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.ertools.demooder.utils.AppConstants

object NotificationBuilder{
    fun buildChannels(context: Context) {
        val microphoneChannel = NotificationChannel(
            AppConstants.NOTIFICATION_MICROPHONE_CHANNEL_ID,
            AppConstants.NOTIFICATION_MICROPHONE_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val mediaChannel = NotificationChannel(
            AppConstants.NOTIFICATION_MEDIA_CHANNEL_ID,
            AppConstants.NOTIFICATION_MEDIA_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannels(
            listOf(microphoneChannel, mediaChannel)
        )
    }
}