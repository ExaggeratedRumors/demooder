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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.unit.dp
import com.ertools.demooder.R
import com.ertools.demooder.core.recorder.AudioRecorder
import com.ertools.demooder.core.recorder.SpectrumProvider
import com.ertools.demooder.presentation.theme.Colors.AV_A_BTN_COLOR
import com.ertools.demooder.presentation.theme.Colors.AV_C_BTN_COLOR
import com.ertools.demooder.presentation.theme.Colors.AV_START_BTN_COLOR
import com.ertools.demooder.presentation.theme.Colors.AV_STOP_BTN_COLOR
import com.ertools.demooder.presentation.theme.Strings
import com.ertools.processing.signal.SignalPreprocessor.applyWeighting
import com.ertools.processing.signal.SignalPreprocessor.toDecibels
import com.ertools.processing.signal.Weighting.WeightingType
import kotlinx.coroutines.delay

@Composable
fun AudioVisualizer() {
    val context = LocalContext.current
    val recorder = remember { AudioRecorder(context) }
    val isRecording = remember { mutableStateOf(false) }
    val weightingState = remember { mutableStateOf(WeightingType.A_WEIGHTING) }
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
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.prediction_buttons_space)))
            DampingButton(weightingState)

        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.prediction_buttons_space)))
        Graph(provider = recorder, state = weightingState)
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
        modifier = Modifier.size(dimensionResource(R.dimen.prediction_button_size)),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isRecording.value) AV_STOP_BTN_COLOR else AV_START_BTN_COLOR
        )
    ) {
        Text(text = if (isRecording.value) Strings.AV_STOP_BTN_NAME else Strings.AV_START_BTN_NAME)
    }
}

@Composable
fun DampingButton(weightingState: MutableState<WeightingType>) {
    Button(
        onClick = {
            if(weightingState.value == WeightingType.A_WEIGHTING) {
                weightingState.value = WeightingType.C_WEIGHTING
            } else {
                weightingState.value = WeightingType.A_WEIGHTING
            }
        },
        modifier = Modifier.size(dimensionResource(R.dimen.prediction_button_size)),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (weightingState.value == WeightingType.A_WEIGHTING)
                AV_A_BTN_COLOR
            else
                AV_C_BTN_COLOR
        )
    ) {
        Text(
            text = if (weightingState.value == WeightingType.A_WEIGHTING)
                Strings.AV_A_BTN_NAME
            else
                Strings.AV_C_BTN_NAME
        )
    }
}

@Composable
fun Graph (provider: SpectrumProvider, state: MutableState<WeightingType>) {
    var spectrum by remember { mutableStateOf(provider.getAmplitudeSpectrum()) }

    LaunchedEffect(key1 = true) {
        while (true) {
            spectrum =
                provider.getAmplitudeSpectrum()
                    .toDecibels()
                    .applyWeighting(state.value)
            delay(100L)
        }
    }

    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.equalizer_height))
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.large
            ),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            spectrum.forEach { sample ->
                val animatedHeight by animateFloatAsState(
                    targetValue = sample.toFloat() * integerResource(R.integer.equalizer_bar_factor),
                    animationSpec = spring(),
                    label = "$sample"
                )
                Box(
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.equalizer_bar_height))
                        .height((animatedHeight).dp)
                        .align(Alignment.Bottom)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}