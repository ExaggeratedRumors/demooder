package com.ertools.demooder.core.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ertools.demooder.R
import com.ertools.demooder.utils.AppConstants
import com.ertools.demooder.utils.Permissions

class MicrophoneService: Service() {
    private var isPlaying = false
    private val binder = LocalBinder()
    private lateinit var notificationManager: NotificationManager
    private var onStartCallback: (() -> Unit)? = null
    private var onStopCallback: (() -> Unit)? = null

    inner class LocalBinder : Binder() {
        fun getService(): MicrophoneService = this@MicrophoneService
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("PlayerService", "Received command: ${intent?.action}")

        val data = intent?.toNotificationData()
        when(intent?.action) {
            AppConstants.NOTIFICATION_ACTION_STOP -> {
                isPlaying = false
                onPausedNotification(data)
                onStopCallback?.invoke()
            }
            AppConstants.NOTIFICATION_ACTION_START -> {
                isPlaying = true
                onRunningNotification(data)
                onStartCallback?.invoke()
            }
            AppConstants.NOTIFICATION_ACTION_NOTIFY -> {
                onRunningNotification(data)
            }
            else -> {
                Log.d("PlayerService", "Unknown action: ${intent?.action}")
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    /**************/
    /** Private **/
    /*************/
    private fun onRunningNotification(data: NotificationData?) {
        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0, 1, 2)
        val notification = NotificationCompat.Builder(this, AppConstants.NOTIFICATION_MEDIA_CHANNEL_ID)
            .setStyle(style)
            .setContentTitle(data?.title ?: "")
            .setContentText(data?.subtitle ?: "")
            .addAction(
                R.drawable.stop_filled,
                AppConstants.NOTIFICATION_ACTION_STOP,
                pendingIntentOf(AppConstants.NOTIFICATION_ACTION_STOP, 0)
            ).setSmallIcon(R.drawable.vec_home_speech)
            .build()
        if(Permissions.isPostNotificationPermissionGained(this)) {
            Log.d("PlayerService", "Starting foreground service with running notification")
            startForeground(AppConstants.NOTIFICATION_MEDIA_ID, notification)
        }
    }

    private fun onPausedNotification(data: NotificationData?) {
        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0, 1, 2)
        val notification = NotificationCompat.Builder(this, AppConstants.NOTIFICATION_MEDIA_CHANNEL_ID)
            .setStyle(style)
            .setContentTitle(data?.title ?: "")
            .setContentText(data?.subtitle ?: "")
            .addAction(
                R.drawable.mic_filled,
                AppConstants.NOTIFICATION_ACTION_START,
                pendingIntentOf(AppConstants.NOTIFICATION_ACTION_START, 0)
            ).setSmallIcon(R.drawable.vec_home_speech)
            .build()
        if(Permissions.isPostNotificationPermissionGained(this)) {
            Log.d("PlayerService", "Starting foreground service with pause notification")
            startForeground(AppConstants.NOTIFICATION_MEDIA_ID, notification)
        }
    }

    private fun pendingIntentOf(action: String, requestCode: Int = 0): PendingIntent {
        val intent = Intent(this, PlayerService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun Intent.toNotificationData(): NotificationData? {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.getParcelableExtra(AppConstants.NOTIFICATION_DATA, NotificationData::class.java)
        else
            this.getParcelableExtra(AppConstants.NOTIFICATION_DATA)

    }
}