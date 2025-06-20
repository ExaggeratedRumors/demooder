package com.ertools.demooder.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily


@Composable
fun TitleValue(
    modifier: Modifier,
    title: String,
    value: String,
    isVertical: Boolean = true
) {
    if(isVertical) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.primary
            )
        }
    } else {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}