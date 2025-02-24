package com.ertools.demooder.presentation.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ertools.demooder.R
import com.ertools.demooder.core.classifier.ClassifierManager
import com.ertools.demooder.core.recorder.AudioRecorder
import com.ertools.demooder.presentation.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@Composable
fun PredictionView(
) {
    /** Recorder **/
    val context = LocalContext.current
    val recorder = remember { AudioRecorder(context) }

    /** Buttons state **/
    val isRecording = remember { mutableStateOf(false) }
    val isClear = remember { mutableStateOf(false) }
    val isSave = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        if (isRecording.value) {
            recorder.startRecording()
        } else {
            recorder.stopRecording()
        }
        SpectrumView(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(16.dp),
            recorder = recorder
        )
        EvaluationLabel(
            modifier = Modifier.fillMaxHeight(0.25f),
            context = context,
            recorder = recorder,
            isRecording
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
    val counter = remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = true) {
        while (isRecording.value) {
            val detectionPeriod = settingsViewModel.signalDetectionPeriod.first()
            prediction.value = classifier.predict(recorder.getData())
            counter.intValue += 1
            Log.i("PredictionView", "Prediction: ${prediction.value}, counter: $counter")
            delay(detectionPeriod.toLong() * 1000)
        }
    }

    Column(
        modifier = modifier
    ) {
        if(isRecording.value) {
            Text(
                text = prediction.value.joinToString("\n") { "Prediction [$counter]: ${it.first}" },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}