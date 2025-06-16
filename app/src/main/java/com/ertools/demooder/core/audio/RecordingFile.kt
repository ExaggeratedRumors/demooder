package com.ertools.demooder.core.audio

import android.net.Uri

data class RecordingFile(
    val name: String,
    val modificationDate: String,
    val size: String,
    val uri: Uri
)