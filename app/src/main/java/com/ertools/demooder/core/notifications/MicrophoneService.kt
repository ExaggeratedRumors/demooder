package com.ertools.demooder.core.notifications

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ertools.demooder.R
import com.ertools.demooder.core.host.MainActivity

class MicrophoneService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1
        const val ACTION_STOP = "ACTION_STOP"
        const val CHANNEL_ID = "audio_service_channel"
    }

    /******************/
    /* Implementation */
    /******************/

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, buildNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent?.action == ACTION_STOP) stopSelf()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    /*************/
    /** Private **/
    /*************/
    private fun buildNotification(): Notification {
        val stopIntent = Intent(this, MicrophoneService::class.java).apply {
            action = ACTION_STOP
        }

        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val openAppIntent = Intent(this, MainActivity::class.java)
        val openAppPendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Demooder")
            .setContentText("Running...")
            .setSmallIcon(R.drawable.vec_home_speech)
            .setContentIntent(openAppPendingIntent)
            .addAction(R.drawable.stop_filled, "Stop", stopPendingIntent)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1))
            .setOngoing(true)
            .build()
        return notification
    }

}