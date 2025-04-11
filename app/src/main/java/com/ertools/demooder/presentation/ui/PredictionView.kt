package com.ertools.demooder.presentation.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ertools.demooder.R
import com.ertools.demooder.core.classifier.ClassifierManager
import com.ertools.demooder.core.recorder.AudioRecorder
import com.ertools.demooder.core.recorder.SpectrumProvider
import com.ertools.demooder.presentation.viewmodel.SettingsViewModel
import com.ertools.demooder.utils.AppConstants
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import java.util.Locale

@Composable
fun PredictionView(
) {
    /** Recorder **/
    val context = LocalContext.current
    val recorder = remember { AudioRecorder() }

    /** Buttons state **/
    val isRecording = remember { mutableStateOf(false) }
    val isRecorderInitialized = remember { mutableStateOf(false) }
    val isClear = remember { mutableStateOf(false) }
    val isSave = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        if (isRecording.value) {
            recorder.startRecording {
                isRecorderInitialized.value = true
            }
        } else {
            recorder.stopRecording {
                isRecorderInitialized.value = false
            }
        }
        SpectrumView(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(16.dp),
            provider = recorder,
            isRecording = isRecorderInitialized
        )
        EvaluationLabel(
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth(0.6f)
                .background(MaterialTheme.colorScheme.secondary)
                .align(Alignment.CenterHorizontally),
            context = context,
            recorder = recorder,
            isRecording = isRecorderInitialized
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StateButton(
                state = isSave,
                enableIconResource = R.drawable.settings_link,
                disableIconResource = R.drawable.settings_link,
                iconContentDescriptionResource = R.string.prediction_save_cd,
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .aspectRatio(1f)
            )
            StateButton(
                state = isRecording,
                enableIconResource = R.drawable.mic_filled,
                disableIconResource = R.drawable.stop_filled,
                iconContentDescriptionResource = R.string.prediction_record_cd,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .aspectRatio(1f)
            )
            StateButton(
                state = isClear,
                enableIconResource = R.drawable.records_filled,
                disableIconResource = R.drawable.records_outline,
                iconContentDescriptionResource = R.string.prediction_clear_cd,
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .aspectRatio(1f)
            )
        }
        Spacer(modifier = Modifier.fillMaxHeight())
    }
}

@Composable
fun StateButton(
    state: MutableState<Boolean>,
    enableIconResource: Int,
    disableIconResource: Int,
    iconContentDescriptionResource: Int,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            state.value = !state.value
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (state.value) MaterialTheme.colorScheme.tertiary
            else MaterialTheme.colorScheme.primary
        ),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Icon(
            painter = if(state.value) painterResource(disableIconResource)
            else painterResource(enableIconResource),
            modifier = Modifier.fillMaxSize(),
            contentDescription = stringResource(iconContentDescriptionResource)
        )
    }
}


@Composable
fun SpectrumView(
    modifier: Modifier = Modifier,
    provider: SpectrumProvider,
    isRecording: MutableState<Boolean>
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ){
        var spectrum by remember { mutableStateOf(DoubleArray(0)) }

        LaunchedEffect(key1 = true) {
            while (true) {
                if(isRecording.value) spectrum = provider.getOctavesAmplitudeSpectrum()
                delay(AppConstants.UI_GRAPH_UPDATE_DELAY)
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
}

@Composable
fun EvaluationLabel(
    modifier: Modifier = Modifier,
    context: Context,
    recorder: AudioRecorder,
    isRecording: MutableState<Boolean>
) {
    val settingsViewModel: SettingsViewModel = viewModel()
    val classifier = ClassifierManager().apply {
        this.loadClassifier(context)
    }
    val prediction = remember { mutableStateOf(listOf<Pair<String, Float>>()) }
    val placeholderText = stringResource(R.string.prediction_result_placeholder)
    val classificationText = remember { mutableStateOf(placeholderText) }

    LaunchedEffect(key1 = true) {
        while (true) {
            val detectionPeriod = settingsViewModel.signalDetectionPeriod.first()
            if(isRecording.value) {
                coroutineScope {
                    classifier.predict(recorder.getData()) {
                        prediction.value = it
                        classificationText.value = prediction.value.joinToString("\n") {
                                (label, inference) -> "${label}: ${"%.2f".format(Locale.ENGLISH, inference * 100)}%"
                        }
                    }
                }
            }
            delay(detectionPeriod.toLong() * 1000)
        }
    }

    Column(
        modifier = modifier
    ) {

        Text(
            text = classificationText.value,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}
