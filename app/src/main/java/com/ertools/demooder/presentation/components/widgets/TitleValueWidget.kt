package com.ertools.demooder.presentation.components.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow


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
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}