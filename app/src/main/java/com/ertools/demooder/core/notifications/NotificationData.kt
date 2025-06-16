package com.ertools.demooder.core.notifications

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationData(
    val title: String,
    val subtitle: String
): Parcelable