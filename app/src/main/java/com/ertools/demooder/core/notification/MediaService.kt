package com.ertools.demooder.core.notification

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ertools.demooder.R
import com.ertools.demooder.core.host.MainActivity
import com.ertools.demooder.utils.AppConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MediaService: Service() {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    /*********************/
    /** Implementations **/
    /*********************/

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        updateNotification(NotificationData(action = NotificationAction.INIT))
        observeEvents()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        Log.d("MediaService", "Service is being destroyed")
        super.onDestroy()
        job.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    /**************/
    /** Private **/
    /*************/

    private fun observeEvents() {
        scope.launch {
            NotificationEventStream.events.collect { data ->
                Log.d("MediaService", "Received notification event: $data")
                when(data.action) {
                    NotificationAction.DESTROY -> {
                        onDestroy()
                    }
                    else -> updateNotification(data)
                }
            }
        }
    }

    private fun updateNotification(data: NotificationData?) {
        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0)

        val icon =  if(data?.action == NotificationAction.START) R.drawable.stop_filled
                    else R.drawable.mic_filled

        val action =    if(data?.action == NotificationAction.START) AppConstants.NOTIFICATION_ACTION_STOP
                        else AppConstants.NOTIFICATION_ACTION_START

        /** Action intent **/
        val actionData = NotificationData(
            action =    if(data?.action == NotificationAction.START) NotificationAction.STOP
                        else NotificationAction.START,
        )

        val actionIntent = Intent(this, NotificationReceiver::class.java).apply {
            putExtra(AppConstants.NOTIFICATION_DATA, actionData)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val actionPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            actionIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        /** Open app intent **/
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val openAppPendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, AppConstants.NOTIFICATION_MEDIA_CHANNEL_ID)
            .setStyle(style)
            .setContentTitle(data?.title ?: "")
            .setContentText(data?.subtitle ?: "")
            .setContentIntent(openAppPendingIntent)
            .addAction(
                icon,
                action,
                actionPendingIntent
            ).setSmallIcon(R.drawable.vec_home_speech)
            .build()
        startForeground(AppConstants.NOTIFICATION_MEDIA_ID, notification)
    }
}