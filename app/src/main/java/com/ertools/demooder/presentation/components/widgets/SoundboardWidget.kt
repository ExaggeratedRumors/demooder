package com.ertools.demooder.presentation.components.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SoundboardWidget(
    modifier: Modifier,
    mainButton: @Composable () -> Unit,
    leftButton: @Composable () -> Unit,
    rightButton: @Composable () -> Unit
) {
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        leftButton()
        mainButton()
        rightButton()
    }
}