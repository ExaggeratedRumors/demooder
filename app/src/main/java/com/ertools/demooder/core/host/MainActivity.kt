package com.ertools.demooder.core.host

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ertools.demooder.presentation.navigation.OutsideNavigation
import com.ertools.demooder.presentation.theme.Theme
import com.ertools.demooder.utils.Permissions
import com.ertools.demooder.utils.Permissions.REQUIRED_PERMISSIONS

class MainActivity : ComponentActivity() {
    /********************/
    /** Implementation **/
    /********************/

    override fun onStart() {
        super.onStart()
        Permissions.requestPermissions(this)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        try {
            setContent {
                Theme.MainTheme {
                    OutsideNavigation()
                }
            }
        } catch (e: Exception) {
            cancelNotifcations(this)
        }
    }

    /*********************/
    /** Private methods **/
    /*********************/

    private fun cancelNotifcations(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

}