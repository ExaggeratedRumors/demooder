package com.ertools.demooder.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ertools.demooder.R

@Composable
fun PredictionView(
) {
    val isRecording = remember { mutableStateOf(false) }
    val isClear = remember { mutableStateOf(false) }
    val isSave = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        AudioVisualizer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .padding(16.dp),
            isRecording = isRecording
        )
        EvaluationLabel(modifier = Modifier.fillMaxHeight(0.3f))
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StateButton(
                state = isSave,
                enableIconResource = R.drawable.settings_link,
                disableIconResource = R.drawable.settings_link,
                iconContentDescriptionResource = R.string.prediction_save_cd,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .aspectRatio(1f)
            )
            StateButton(
                state = isRecording,
                enableIconResource = R.drawable.mic_filled,
                disableIconResource = R.drawable.stop_filled,
                iconContentDescriptionResource = R.string.prediction_record_cd,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
            StateButton(
                state = isClear,
                enableIconResource = R.drawable.records_filled,
                disableIconResource = R.drawable.records_outline,
                iconContentDescriptionResource = R.string.prediction_clear_cd,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
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
fun EvaluationLabel(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Text("test")
    }
}