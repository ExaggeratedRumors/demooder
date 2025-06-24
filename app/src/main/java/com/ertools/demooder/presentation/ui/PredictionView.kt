package com.ertools.demooder.presentation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ertools.demooder.R
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.classifier.PredictionProvider
import com.ertools.demooder.core.detector.DetectionProvider
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.demooder.core.settings.SettingsStore
import com.ertools.demooder.core.spectrum.SpectrumProvider
import com.ertools.demooder.presentation.components.AppBar
import com.ertools.demooder.presentation.components.ClickButton
import com.ertools.demooder.presentation.components.StateButton
import com.ertools.demooder.presentation.components.TitleValue
import com.ertools.demooder.presentation.viewmodel.AudioViewModel
import com.ertools.demooder.presentation.viewmodel.AudioViewModelFactory
import com.ertools.demooder.presentation.viewmodel.ProviderViewModel
import com.ertools.demooder.presentation.viewmodel.SettingsViewModel
import com.ertools.demooder.presentation.viewmodel.SettingsViewModelFactory
import com.ertools.demooder.presentation.viewmodel.StatisticsViewModel
import com.ertools.demooder.utils.AppConstants
import com.ertools.processing.commons.Emotion
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun PredictionView(
    providerViewModel: ProviderViewModel
) {
    val audioProvider = providerViewModel.currentProvider.collectAsState().value
    val context = LocalContext.current

    /** Settings **/
    val settingsStore = SettingsStore(context)
    val settingsViewModelFactory = remember {
        SettingsViewModelFactory(
            settingsStore = settingsStore
        )
    }
    val settingsViewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)
    val detectionPeriodSeconds = settingsViewModel.signalDetectionPeriod.collectAsState()

    /** Recorder **/
    val classifier = EmotionClassifier().apply { this.loadClassifier(context) }
    val detector = SpeechDetector().apply { this.loadModel(context) }
    val recorderViewModelFactory = remember {
        AudioViewModelFactory(
            audioProvider = audioProvider,
            classifier = classifier,
            detector = detector,
            settingsStore = settingsStore
        )
    }
    val audioViewModel: AudioViewModel = viewModel<AudioViewModel>(factory = recorderViewModelFactory).apply {
        runNotificationListeningTask(context)
    }
    val statisticsViewModel: StatisticsViewModel = viewModel()

    /** Buttons state **/
    val isRecording = audioViewModel.isWorking.collectAsState()

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
            provider = audioViewModel,
            isRecording = isRecording
        )
        EvaluationView(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(0.6f)
                .background(MaterialTheme.colorScheme.secondary)
                .align(Alignment.CenterHorizontally),
            predictionProvider = statisticsViewModel,
            detectionProvider = audioViewModel,
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

            ) { audioViewModel.save() }
            StateButton(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .aspectRatio(1f),
                state = isRecording,
                enableIconResource = R.drawable.mic_filled,
                disableIconResource = R.drawable.stop_filled,
                iconContentDescriptionResource = R.string.prediction_record_cd
            ) { audioViewModel.togglePlay(context) }
            ClickButton(
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .aspectRatio(1f),
                iconResource = R.drawable.records_filled,
                iconContentDescriptionResource = R.string.prediction_clear_cd
            ) { audioViewModel.abort() }
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
    ) {
        /** Flow **/
        val spectrumFlow = remember(provider) { provider.getSpectrum() }
        val spectrum by spectrumFlow.collectAsStateWithLifecycle()

        /** Constance **/
        val labels: List<String> = listOf(
            "16", "32", "64", "128", "256", "512", "1k", "2k", "4k", "8k", "16k"
        )

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.equalizer_height))
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(dimensionResource(R.dimen.spectrum_padding)),
            ) {

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                spectrum.forEachIndexed { i, sample ->
                    val animatedHeight by animateFloatAsState(
                        targetValue = sample.toFloat() * integerResource(R.integer.spectrum_bar_factor),
                        animationSpec = spring(stiffness = Spring.StiffnessMedium),
                        label = "$sample"
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(dimensionResource(R.dimen.spectrum_padding))
                            .fillMaxHeight()
                            .align(Alignment.Bottom)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                    ) {
                        if (isRecording.value) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height((animatedHeight).dp)
                                    .align(Alignment.BottomCenter)
                                    .background(MaterialTheme.colorScheme.primary)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(2.dp)
                                        .background(MaterialTheme.colorScheme.tertiary)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = labels[i],
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                            )

                        }
                    }
                }
            }

            /*Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                labels.forEach { label ->
                    Text(
                        text = label,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
            }*/
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
    detectionPeriodSeconds: State<Double>,
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

    Box(
        modifier = modifier
    ){
        val progressAnimation = remember { Animatable(0f) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.prediction_padding))
        ) {
            if (isRecording.value) {
                TitleValue(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f),
                    title = predictionLabel,
                    value = if (!isSpeech || lastTwoPredictions.isEmpty()) loadingText
                    else lastTwoPredictions[0].let { prediction ->
                        "${prediction.label}: ${
                            "%.2f".format(
                                Locale.ENGLISH,
                                prediction.confidence * 100
                            )
                        }%"
                    },
                    isVertical = true
                )
                LaunchedEffect(Unit) {
                    while (isRecording.value) {
                        progressAnimation.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(
                                durationMillis = (1000 * detectionPeriodSeconds.value).roundToInt(),
                                easing = LinearEasing
                            )
                        )
                        progressAnimation.snapTo(0f)
                    }
                }
                Box(
                    modifier = Modifier.fillMaxHeight(0.1f)
                        .fillMaxWidth(progressAnimation.value)
                        .background(MaterialTheme.colorScheme.primary)
                )
                TitleValue(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f),
                    title = previousPredictionLabel,
                    value = if (!isSpeech || lastTwoPredictions.size < 2) previousPredictionPlaceholderText
                    else lastTwoPredictions[1].let { prediction ->
                        "${prediction.label}: ${"%.2f".format(Locale.ENGLISH, prediction.confidence * 100)}%"
                    },
                    isVertical = false
                )
                TitleValue(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .align(Alignment.Start),
                    title = angerLabel,
                    value = "%.2f".format(Locale.ENGLISH, angryDuration),
                    isVertical = false
                )
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
