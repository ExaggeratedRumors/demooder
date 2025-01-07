package com.ertools.demooder.utils

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ertools.demooder.R

object Validation {
    @Composable
    fun isNumberInRange(value: String, min: Int, max: Int): Boolean {
        val number = value.toDoubleOrNull()
        if(number == null) {
            Toast.makeText(
                null,
                stringResource(R.string.error_number),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        val result = number in min.toDouble()..max.toDouble()
        if(!result) {
            Toast.makeText(
                null,
                stringResource(R.string.error_number_range, min, max),
                Toast.LENGTH_SHORT
            ).show()
        }
        return result
    }
}