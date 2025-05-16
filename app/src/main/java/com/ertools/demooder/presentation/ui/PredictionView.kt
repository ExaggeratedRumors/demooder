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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ertools.demooder.R
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.classifier.PredictionProvider
import com.ertools.demooder.core.detector.DetectionProvider
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.demooder.core.recorder.AudioRecorder
import com.ertools.demooder.core.settings.SettingsStore
import com.ertools.demooder.core.spectrum.SpectrumProvider
import com.ertools.demooder.presentation.components.ClickButton
import com.ertools.demooder.presentation.components.StateButton
import com.ertools.demooder.presentation.viewmodel.RecorderViewModel
import com.ertools.demooder.presentation.viewmodel.RecorderViewModelFactory
import com.ertools.demooder.presentation.viewmodel.SettingsViewModel
import com.ertools.demooder.presentation.viewmodel.SettingsViewModelFactory
import com.ertools.demooder.presentation.viewmodel.StatisticsViewModel
import com.ertools.processing.commons.Emotion
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun PredictionView() {
    val context = LocalContext.current

    /** Settings **/
    val settingsStore = SettingsStore(context)
    val settingsViewModelFactory = remember {
        SettingsViewModelFactory(
            settingsStore = settingsStore
        )
    }
    val settingsViewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)
    val detectionPeriodSeconds by settingsViewModel.signalDetectionPeriod.collectAsState()

    /** Recorder **/
    val classifier = EmotionClassifier().apply { this.loadClassifier(context) }
    val detector = SpeechDetector().apply { this.loadModel(context) }
    val recorder = AudioRecorder()
    val recorderViewModelFactory = remember {
        RecorderViewModelFactory(
            recorder = recorder,
            classifier = classifier,
            detector = detector,
            settingsStore = settingsStore
        )
    }
    val recorderViewModel: RecorderViewModel = viewModel(factory = recorderViewModelFactory)
    val statisticsViewModel: StatisticsViewModel = viewModel()

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
                .fillMaxHeight(0.6f)
                .padding(16.dp),
            provider = recorderViewModel,
            isRecording = isRecording
        )
        EvaluationView(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(0.6f)
                .background(MaterialTheme.colorScheme.secondary)
                .align(Alignment.CenterHorizontally),
            predictionProvider = statisticsViewModel,
            detectionProvider = recorderViewModel,
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
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .aspectRatio(1f),
                iconResource = R.drawable.settings_link,
                iconContentDescriptionResource = R.string.prediction_save_cd

            ) { recorderViewModel.saveRecording() }
            StateButton(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .aspectRatio(1f),
                state = isRecording,
                enableIconResource = R.drawable.mic_filled,
                disableIconResource = R.drawable.stop_filled,
                iconContentDescriptionResource = R.string.prediction_record_cd
            ) { recorderViewModel.toggleRecording() }
            ClickButton(
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .aspectRatio(1f),
                iconResource = R.drawable.records_filled,
                iconContentDescriptionResource = R.string.prediction_clear_cd
            ) { recorderViewModel.clearRecording() }
        }
        Spacer(modifier = Modifier.fillMaxHeight())
    }
}

/**
 * Displays the spectrum of the audio signal.
 * @param modifier Modifier to be applied to the view.
 * @param provider SpectrumProvider giving information about the audio spectrum.
 * @param isRecording State indicating if the recorder is currently recording.
 */
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

/**
 * Displays the prediction result of the classifier.
 * @param modifier Modifier to be applied to the view.
 * @param predictionProvider PredictionProvider giving information about predictions history.
 * @param detectionProvider DetectionProvider giving information about speech detection.
 * @param detectionPeriodSeconds Duration of the detection period in seconds.
 * @param isRecording State indicating if the recorder is currently recording.
 */
@Composable
fun EvaluationView(
    modifier: Modifier = Modifier,
    predictionProvider: PredictionProvider,
    detectionProvider: DetectionProvider,
    detectionPeriodSeconds: Double,
    isRecording: State<Boolean>
) {
    val lastTwoPredictions by predictionProvider.last(2).collectAsState()
    val angryDuration by predictionProvider.proportion(Emotion.ANG).collectAsState()
    val isSpeech by detectionProvider.isSpeech().collectAsState()

    val placeholderText = stringResource(R.string.prediction_placeholder)
    val predictionLabel = stringResource(R.string.prediction_result_label)
    val loadingText = stringResource(R.string.prediction_result_loading)
    val previousPredictionLabel = stringResource(R.string.prediction_previous_label)
    val previousPredictionPlaceholderText = stringResource(R.string.prediction_previous_placeholder)
    val angerLabel = stringResource(R.string.prediction_anger_label)
    BoxWithConstraints(
        modifier = modifier
    ){
        val progressAnimation = remember { Animatable(0f) }
        val maxWidth = constraints.maxWidth.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.prediction_padding))
        ) {
            if (isRecording.value) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = predictionLabel,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = if (!isSpeech || lastTwoPredictions.isEmpty()) loadingText
                        else lastTwoPredictions[0].let { prediction ->
                            "${prediction.label}: ${
                                "%.2f".format(
                                    Locale.ENGLISH,
                                    prediction.confidence * 100
                                )
                            }%"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                LaunchedEffect(Unit) {
                    while (isRecording.value) {
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
                Box(
                    modifier = Modifier.fillMaxHeight(0.1f)
                        .width(maxWidth * progressAnimation.value)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = previousPredictionLabel,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = if (!isSpeech || lastTwoPredictions.size < 2) previousPredictionPlaceholderText
                        else lastTwoPredictions[1].let { prediction ->
                            "${prediction.label}: ${
                                "%.2f".format(
                                    Locale.ENGLISH,
                                    prediction.confidence * 100
                                )
                            }%"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .align(Alignment.Start),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = angerLabel,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = "%.2f".format(
                            Locale.ENGLISH,
                            angryDuration
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = placeholderText,
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
