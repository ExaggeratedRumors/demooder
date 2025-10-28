package com.ertools.demooder.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Permissions {
    internal val REQUIRED_PERMISSIONS: Array<String> = listOf (
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
            Manifest.permission.POST_NOTIFICATIONS
        }
        else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        },
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
            Manifest.permission.FOREGROUND_SERVICE_MICROPHONE
            Manifest.permission.FOREGROUND_SERVICE
        } else {
            Manifest.permission.FOREGROUND_SERVICE
        },
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.SEND_SMS
    ).toTypedArray()

    fun requestPermissions(context: Activity) {
        val permissionsGained = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                context, it
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
        if(!permissionsGained)
            ActivityCompat.requestPermissions(context, REQUIRED_PERMISSIONS, 1)
    }

    fun isPermissionsGained(context: Context): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                context, it
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }

    fun isPostNotificationPermissionGained(context: Context) : Boolean {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun isReadMediaAudioPermissionGained(context: Context) : Boolean {
        return if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_AUDIO
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun isReadExternalStoragePermissionGained(context: Context) : Boolean {
        return if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun isRecordAudioPermissionGained(context: Context) : Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }
}

