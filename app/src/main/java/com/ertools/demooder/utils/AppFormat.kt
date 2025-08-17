package com.ertools.demooder.utils

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppFormat {
    fun timeFormat(ms: Int): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%d:%02d".format(minutes, seconds)
    }

    fun dateFormat(date: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        return sdf.format(Date(date.toLong() * 1000)).toString()
    }
}