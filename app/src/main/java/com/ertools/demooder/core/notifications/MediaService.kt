package com.ertools.demooder.core.notifications

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper.MediaStyle
import com.ertools.demooder.R
import com.ertools.demooder.core.host.MainActivity

class MediaService : Service() {
    companion object {
        const val NOTIFICATION_ID = 1
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_START = "ACTION_START"
        const val CHANNEL_ID = "audio_service_channel"
    }

    private lateinit var mediaSession: MediaSession
    /******************/
    /* Implementation */
    /******************/

    @UnstableApi
    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()

        startForeground(NOTIFICATION_ID, buildNotification(mediaSession))
    }

    @UnstableApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
            ACTION_START -> {
                startForeground(NOTIFICATION_ID, buildNotification(mediaSession))
            }
            else -> {

            }
        }


        if(intent?.action == ACTION_STOP) {

        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    /*************/
    /** Private **/
    /*************/
    @UnstableApi
    private fun buildNotification(mediaSession: MediaSession): Notification {
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
            .setStyle(MediaStyle(mediaSession).setShowActionsInCompactView(0))
            .setOngoing(true)
            .build()
        return notification
    }

}