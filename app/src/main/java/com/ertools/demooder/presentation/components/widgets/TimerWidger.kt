package com.ertools.demooder.presentation.components.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import com.ertools.demooder.presentation.viewmodel.TimerViewModel
import com.ertools.demooder.utils.AppFormat.advancedTimeFormat
import kotlinx.coroutines.delay
import kotlin.concurrent.timer

/**
 * A simple timer widget that displays the elapsed time in hh:mm:ss format.
 * It starts counting when [isRecording] is true.
 *
 * @param modifier Modifier to be applied to the widget.
 * @param timerViewModel ViewModel managing the timer state.
 * @param isRecording State indicating if the recording is active.
 */
@Composable
fun TimerWidget(
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel,
    isRecording: State<Boolean>
) {
    LaunchedEffect(isRecording.value) {
        if (isRecording.value) {
            while (true) {
                delay(100)
                timerViewModel.increment(100)
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = advancedTimeFormat(timerViewModel.timeMs.intValue),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onTertiary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}