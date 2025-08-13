package com.ertools.demooder.core.audio

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecordingFile(
    val name: String,
    val modificationDate: String,
    val size: String,
    val uri: Uri,
    val isWav: Boolean
): Parcelable