package com.ertools.demooder.utils

import java.util.Locale

object Logger {
    fun logSpectrum(spectrum: DoubleArray) {
        if(DEBUG_INTERFACE) {
            val sb = StringBuilder()
            sb.append("DEBUG INTERFACE: ")
            for (i in spectrum.indices) {
                sb.append("%.2f".format(Locale.ENGLISH,spectrum[i]))
                sb.append(" ")
            }
            println(sb.toString())
        }
    }

}