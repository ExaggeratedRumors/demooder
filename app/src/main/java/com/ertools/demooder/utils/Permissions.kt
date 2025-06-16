package com.ertools.demooder.utils

import android.Manifest
import android.content.Context
import androidx.core.content.ContextCompat

object Permissions {
    internal val REQUIRED_PERMISSIONS = listOf (
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
            Manifest.permission.POST_NOTIFICATIONS
        }
        else
            Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO,
    ).toTypedArray()

    fun isPermissionsGained(context: Context): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                context, it
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }

    fun isPostNotificationPermissionGained(context: Context) : Boolean {
        return if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
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

