package com.ertools.demooder.presentation.components.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ertools.demooder.utils.AppFormat.timeFormat
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AudioSeekBarWidget(
    modifier: Modifier,
    durationStateFlow: StateFlow<Int>,
    positionStateFlow: StateFlow<Int>,
    onSeekChange: (Int) -> Unit,
) {
    val duration by durationStateFlow.collectAsState()
    val position by positionStateFlow.collectAsState()
    var sliderPosition by remember(position) { mutableFloatStateOf(position.toFloat()) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = timeFormat(0), modifier = Modifier.weight(1f))

            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                onValueChangeFinished = {
                    onSeekChange(sliderPosition.toInt())
                },
                valueRange = 0f..duration.toFloat(),
                modifier = Modifier.weight(6f)
            )

            Text(text = timeFormat(duration), modifier = Modifier.weight(1f))
        }
        Text(text = timeFormat(position), style = MaterialTheme.typography.bodyMedium)
    }
}
