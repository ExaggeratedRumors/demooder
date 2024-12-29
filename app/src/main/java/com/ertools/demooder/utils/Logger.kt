package com.ertools.demooder.utils

import android.util.Log
import java.util.Locale

object Logger {
    fun logSpectrum(spectrum: DoubleArray) {
        if(DEBUG_MODE) {
            val sb = StringBuilder()
            sb.append("DEBUG INTERFACE: ")
            for (i in spectrum.indices) {
                sb.append("%.2f".format(Locale.ENGLISH,spectrum[i]))
                sb.append(" ")
            }
            Log.i("SPECTRUM", sb.toString())
        }
    }

    fun log(message: String) {
        if (DEBUG_MODE) Log.i("DEBUG", message)
    }

}