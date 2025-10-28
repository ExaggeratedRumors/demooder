package com.ertools.demooder.presentation.components.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ertools.demooder.R
import com.ertools.demooder.core.classifier.Prediction
import com.ertools.demooder.core.classifier.PredictionProvider
import com.ertools.demooder.core.detector.DetectionProvider
import com.ertools.demooder.utils.AppFormat
import com.ertools.processing.commons.Emotion
import kotlin.math.roundToInt


/**
 * Displays the prediction result of the classifier.
 * @param modifier Modifier to be applied to the view.
 * @param predictionProvider PredictionProvider giving information about predictions history.
 * @param detectionProvider DetectionProvider giving information about speech detection.
 * @param detectionPeriodSeconds Duration of the detection period in seconds.
 * @param isRecording State indicating if the recorder is currently recording.
 */
@Composable
fun EvaluationWidget(
    modifier: Modifier = Modifier,
    predictionProvider: PredictionProvider,
    detectionProvider: DetectionProvider,
    detectionPeriodSeconds: State<Double>,
    isRecording: State<Boolean>
) {
    val placeholderText = stringResource(R.string.prediction_placeholder)
    val predictionLabel = stringResource(R.string.prediction_result_label)
    val loadingText = stringResource(R.string.prediction_result_loading)
    val previousPredictionLabel = stringResource(R.string.prediction_previous_label)
    val previousPredictionPlaceholderText = stringResource(R.string.prediction_previous_placeholder)
    val angerLabel = stringResource(R.string.prediction_anger_label)

    val lastTwoPredictions: List<Prediction> by predictionProvider.last(2).collectAsStateWithLifecycle()
    val lastPrediction: MutableState<String> = remember { mutableStateOf(loadingText) }
    val previousPrediction: MutableState<String> = remember { mutableStateOf(previousPredictionPlaceholderText) }
    val angerCounter by predictionProvider.count(Emotion.ANG).collectAsState()
    val isSpeech by detectionProvider.isSpeech().collectAsState()
    val detectionCounter by detectionProvider.detectionCounter().collectAsState()

    Box(
        modifier = modifier
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.prediction_bar_padding))
        ) {
            val progressAnimation = remember { Animatable(0f) }
            if (isRecording.value) {
                LaunchedEffect(detectionCounter) {
                    lastPrediction.value =
                        if (lastTwoPredictions.isEmpty()) loadingText
                        else if(isSpeech) lastTwoPredictions.last().let {
                            "${it.label}: ${AppFormat.floatToTwoPrecString(it.confidence * 100)}%"
                        }
                        else loadingText

                    previousPrediction.value =
                        if (lastTwoPredictions.isEmpty() || (isSpeech && lastTwoPredictions.size == 1))
                            previousPredictionPlaceholderText
                        else lastTwoPredictions[0].let {
                            "${it.label} (${AppFormat.floatToTwoPrecString(it.confidence * 100)}%)"
                        }

                    progressAnimation.snapTo(0f)
                    while (isRecording.value) {
                        progressAnimation.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(
                                durationMillis = (1000 * detectionPeriodSeconds.value).roundToInt(),
                                easing = LinearEasing
                            )
                        )
                    }
                }

                TitleValueWidget(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f),
                    title = predictionLabel,
                    value = lastPrediction.value,
                    isVertical = true
                )

                Box(
                    modifier = Modifier.fillMaxHeight(0.1f)
                        .fillMaxWidth(progressAnimation.value)
                        .background(MaterialTheme.colorScheme.primary)
                )

                TitleValueWidget(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f),
                    title = previousPredictionLabel,
                    value = previousPrediction.value,
                    isVertical = false
                )

                TitleValueWidget(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .align(Alignment.Start),
                    title = angerLabel,
                    value = "${angerCounter * detectionPeriodSeconds.value}s",
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