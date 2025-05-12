package com.ertools.demooder.utils

import android.Manifest
import android.content.Context
import androidx.core.content.ContextCompat

object Permissions {
    internal val REQUIRED_PERMISSIONS = listOf (
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_AUDIO
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
}

