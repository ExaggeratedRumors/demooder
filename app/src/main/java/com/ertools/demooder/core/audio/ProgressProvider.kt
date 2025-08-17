package com.ertools.demooder.core.audio

interface ProgressProvider {
    fun getSize(): Int
    fun getCurrentPosition(): Int
    fun seekTo(position: Int)
}