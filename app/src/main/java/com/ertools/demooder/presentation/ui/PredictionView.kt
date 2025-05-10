package com.ertools.demooder.presentation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.classifier.PredictionProvider
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.demooder.core.recorder.AudioRecorder
import com.ertools.demooder.core.spectrum.SpectrumProvider
import com.ertools.demooder.presentation.viewmodel.RecorderViewModel
import com.ertools.demooder.presentation.viewmodel.RecorderViewModelFactory
import com.ertools.demooder.presentation.viewmodel.SettingsViewModel
import com.ertools.demooder.utils.AppConstants
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun PredictionView(
) {
    /** Settings **/
    val settingsViewModel: SettingsViewModel = viewModel()
    val detectionPeriodSeconds by settingsViewModel.signalDetectionPeriod.collectAsState()

    /** Recorder **/
    val context = LocalContext.current
    val classifier = EmotionClassifier().apply { this.loadClassifier(context) }
    val detector = SpeechDetector().apply { this.loadModel(context) }
    val recorder = AudioRecorder()
    val recorderViewModelFactory = remember {
        RecorderViewModelFactory(
            recorder = recorder,
            classifier = classifier,
            detector = detector,
            graphUpdatePeriodMillis = AppConstants.UI_GRAPH_UPDATE_DELAY,
            classificationPeriodMillis = detectionPeriodSeconds.toLong() * 1000
        )
    }
    val recorderViewModel: RecorderViewModel = viewModel(factory = recorderViewModelFactory)

    /** Buttons state **/
    val isRecording = recorderViewModel.isRecording.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        SpectrumView(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(16.dp),
            provider = recorderViewModel,
            isRecording = isRecording
        )
        EvaluationLabel(
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth(0.6f)
                .background(MaterialTheme.colorScheme.secondary)
                .align(Alignment.CenterHorizontally),
            provider = recorderViewModel,
            detectionPeriodSeconds = detectionPeriodSeconds,
            isRecording = isRecording
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClickButton(
                onClick = { recorderViewModel.saveRecording() },
                iconResource = R.drawable.settings_link,
                iconContentDescriptionResource = R.string.prediction_save_cd,
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .aspectRatio(1f)
            )
            StateButton(
                onClick = { recorderViewModel.toggleRecording() },
                state = isRecording,
                enableIconResource = R.drawable.mic_filled,
                disableIconResource = R.drawable.stop_filled,
                iconContentDescriptionResource = R.string.prediction_record_cd,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .aspectRatio(1f)
            )
            ClickButton(
                onClick = { recorderViewModel.clearRecording() },
                iconResource = R.drawable.records_filled,
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
    onClick: () -> Unit,
    state: State<Boolean>,
    enableIconResource: Int,
    disableIconResource: Int,
    iconContentDescriptionResource: Int,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick() },
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
fun ClickButton(
    onClick: () -> Unit,
    iconResource: Int,
    iconContentDescriptionResource: Int,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Icon(
            painter = painterResource(iconResource),
            modifier = Modifier.fillMaxSize(),
            contentDescription = stringResource(iconContentDescriptionResource)
        )
    }
}

@Composable
fun SpectrumView(
    modifier: Modifier = Modifier,
    provider: SpectrumProvider,
    isRecording: State<Boolean>
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ){
        val spectrum by provider.getSpectrum().collectAsState()

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
                if (isRecording.value) {
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
}

@Composable
fun EvaluationLabel(
    modifier: Modifier = Modifier,
    provider: PredictionProvider,
    detectionPeriodSeconds: Double,
    isRecording: State<Boolean>
) {
    val placeholderText = stringResource(R.string.prediction_result_placeholder)
    val loadingText = stringResource(R.string.prediction_result_loading)
    val prediction by provider.getPrediction().collectAsState(initial = emptyList())

    BoxWithConstraints(
        modifier = modifier
    ){
        val progressAnimation = remember { Animatable(0f) }
        Text(
            text = if(!isRecording.value) placeholderText
            else if (prediction.isEmpty()) loadingText
            else prediction.take(2).joinToString("\n") { (label, inference) ->
                "${label}: ${"%.2f".format(Locale.ENGLISH, inference * 100)}%"
            },
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Center),
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSecondary
        )
        if(!isRecording.value) return@BoxWithConstraints
        LaunchedEffect(Unit) {
            while(isRecording.value) {
                progressAnimation.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = (1000 * detectionPeriodSeconds).roundToInt(),
                        easing = LinearEasing
                    )
                )
                progressAnimation.snapTo(0f)
            }
        }
        Box(modifier = Modifier.height(5.dp)
            .width(maxWidth * progressAnimation.value)
            .background(MaterialTheme.colorScheme.primary)
            .align(Alignment.BottomStart)
        )
    }
}
