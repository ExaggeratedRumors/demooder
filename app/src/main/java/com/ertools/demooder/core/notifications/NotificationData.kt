package com.ertools.demooder.core.notifications

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationData(
    val action: NotificationAction,
    val title: String? = null,
    val subtitle: String? = null
): Parcelable

enum class NotificationAction {
    START,
    STOP,
    UPDATE
}