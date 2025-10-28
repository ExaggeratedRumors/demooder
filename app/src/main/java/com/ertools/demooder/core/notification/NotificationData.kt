package com.ertools.demooder.core.notification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationData(
    val action: NotificationAction,
    val title: String? = null,
    val subtitle: String? = null
): Parcelable

enum class NotificationAction {
    INIT,
    START_FROM_UI,
    STOP_FROM_UI,
    START_FROM_SERVICE,
    STOP_FROM_SERVICE,
    UPDATE,
    DESTROY
}