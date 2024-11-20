package com.ertools.demooder.utils

object Logger {
    fun logSpectrum(spectrum: IntArray) {
        if(DEBUG_INTERFACE) {
            val sb = StringBuilder()
            sb.append("DEBUG INTERFACE: ")
            for (i in spectrum.indices) {
                sb.append(spectrum[i])
                sb.append(" ")
            }
            println(sb.toString())
        }
    }

}