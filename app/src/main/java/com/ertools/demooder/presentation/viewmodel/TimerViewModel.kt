package com.ertools.demooder.presentation.viewmodel

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import com.ertools.demooder.presentation.components.interfaces.Resetable

class TimerViewModel: ViewModel(), Resetable {
    val timeMs: MutableIntState = mutableIntStateOf(0)

    fun increment(value: Int) {
        timeMs.intValue += value
    }

    override fun reset() {
        timeMs.intValue = 0
    }

}