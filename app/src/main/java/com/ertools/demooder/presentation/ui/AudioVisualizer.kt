package com.ertools.demooder.presentation.ui

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ertools.demooder.core.recorder.AudioRecorder
import com.ertools.demooder.core.recorder.DampingState
import com.ertools.demooder.core.recorder.SpectrumProvider
import com.ertools.demooder.core.recorder.getDbSignalForm
import com.ertools.demooder.presentation.theme.AV_BAR_SIZE
import com.ertools.demooder.presentation.theme.AV_BUTTONS_SIZE
import com.ertools.demooder.presentation.theme.AV_GRAPH_SIZE
import com.ertools.demooder.presentation.theme.AV_OBJECTS_GAP_SIZE
import com.ertools.demooder.presentation.theme.AV_SPACE_BETWEEN_BARS_SIZE
import com.ertools.demooder.presentation.theme.Colors.AV_A_BTN_COLOR
import com.ertools.demooder.presentation.theme.Colors.AV_C_BTN_COLOR
import com.ertools.demooder.presentation.theme.Colors.AV_START_BTN_COLOR
import com.ertools.demooder.presentation.theme.Colors.AV_STOP_BTN_COLOR
import com.ertools.demooder.presentation.theme.Strings
import com.ertools.demooder.utils.INVALIDATE_GUI_DELAY
import com.ertools.demooder.utils.Logger
import kotlinx.coroutines.delay

@Composable
fun AudioVisualizer(context: Context) {
    val recorder = remember { AudioRecorder(context) }
    val isRecording = remember { mutableStateOf(false) }
    val dampingState = remember { mutableStateOf(DampingState.A_WEIGHTING) }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            ) {
            RecordButton(recorder, isRecording)
            Spacer(modifier = Modifier.width(AV_OBJECTS_GAP_SIZE))
            DampingButton(dampingState)

        }
        Spacer(modifier = Modifier.height(AV_OBJECTS_GAP_SIZE))
        Graph(provider = recorder, state = dampingState)
    }
}

@Composable
fun RecordButton(recorder: AudioRecorder, isRecording: MutableState<Boolean>) {
    Button(
        onClick = {
            isRecording.value = if (isRecording.value) {
                recorder.stopRecording()
                false
            } else {
                recorder.startRecording()
                true
            }
        },
        modifier = Modifier.size(AV_BUTTONS_SIZE),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isRecording.value) AV_STOP_BTN_COLOR else AV_START_BTN_COLOR
        )
    ) {
        Text(text = if (isRecording.value) Strings.AV_STOP_BTN_NAME else Strings.AV_START_BTN_NAME)
    }
}

@Composable
fun DampingButton(dampingState: MutableState<DampingState>) {
    Button(
        onClick = {
            if(dampingState.value == DampingState.A_WEIGHTING) {
                dampingState.value = DampingState.C_WEIGHTING
            } else {
                dampingState.value = DampingState.A_WEIGHTING
            }
        },
        modifier = Modifier.size(AV_BUTTONS_SIZE),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (dampingState.value == DampingState.A_WEIGHTING)
                AV_A_BTN_COLOR
            else
                AV_C_BTN_COLOR
        )
    ) {
        Text(
            text = if (dampingState.value == DampingState.A_WEIGHTING)
                Strings.AV_A_BTN_NAME
            else
                Strings.AV_C_BTN_NAME
        )
    }
}

@Composable
fun Graph (provider: SpectrumProvider, state: MutableState<DampingState>) {
    var spectrum by remember { mutableStateOf(provider.getAmplitudeSpectrum()) }

    LaunchedEffect(key1 = true) {
        while (true) {
            spectrum = getDbSignalForm(provider.getAmplitudeSpectrum(), state.value)
            delay(INVALIDATE_GUI_DELAY)
            Logger.logSpectrum(spectrum)
        }
    }

    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .height(AV_GRAPH_SIZE)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.large
            ),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight(),
        ) {
            spectrum.forEach { sample ->
                val animatedHeight by animateFloatAsState(
                    targetValue = sample.toFloat() * 10f,
                    animationSpec = spring(),
                    label = "$sample"
                )
                Box(
                    modifier = Modifier
                        .width(AV_BAR_SIZE)
                        .height((animatedHeight).dp)
                        .align(Alignment.Bottom)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(
                    modifier = Modifier
                        .width(AV_SPACE_BETWEEN_BARS_SIZE)
                        .fillMaxHeight()
                )
            }
        }
    }
}