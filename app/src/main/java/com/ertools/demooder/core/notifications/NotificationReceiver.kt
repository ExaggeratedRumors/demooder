package com.ertools.demooder.core.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.ertools.demooder.utils.AppConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onReceive(context: Context, intent: Intent) {
        val data: NotificationData? = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(AppConstants.NOTIFICATION_DATA, NotificationData::class.java)
        } else {
            intent.getParcelableExtra(AppConstants.NOTIFICATION_DATA)
        }
        Log.d("NotificationReceiver", "Received data: $data")
        data?.let {
            scope.launch {
                NotificationEventStream.events.emit(data)
            }
        }
    }
}