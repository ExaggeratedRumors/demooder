package com.ertools.demooder.presentation.ui

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ertools.demooder.R
import com.ertools.demooder.core.audio.AudioProvider
import com.ertools.demooder.core.audio.AudioRecorder
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.classifier.PredictionProvider
import com.ertools.demooder.core.classifier.PredictionRepository
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.demooder.core.settings.SettingsStore
import com.ertools.demooder.presentation.components.dialog.ClickButton
import com.ertools.demooder.presentation.components.dialog.StateButton
import com.ertools.demooder.presentation.components.interfaces.Resetable
import com.ertools.demooder.presentation.components.widgets.EmotionStatisticsWidget
import com.ertools.demooder.presentation.components.widgets.EvaluationWidget
import com.ertools.demooder.presentation.components.widgets.SoundboardWidget
import com.ertools.demooder.presentation.components.widgets.SpectrumWidget
import com.ertools.demooder.presentation.components.widgets.TimerWidget
import com.ertools.demooder.presentation.viewmodel.AlertViewModel
import com.ertools.demooder.presentation.viewmodel.AlertViewModelFactory
import com.ertools.demooder.presentation.viewmodel.AudioViewModel
import com.ertools.demooder.presentation.viewmodel.AudioViewModelFactory
import com.ertools.demooder.presentation.viewmodel.NotificationViewModel
import com.ertools.demooder.presentation.viewmodel.NotificationViewModelFactory
import com.ertools.demooder.presentation.viewmodel.SettingsViewModel
import com.ertools.demooder.presentation.viewmodel.SettingsViewModelFactory
import com.ertools.demooder.presentation.viewmodel.StatisticsViewModel
import com.ertools.demooder.presentation.viewmodel.TimerViewModel

@Composable
fun RecorderView() {
    val audioRecorder = AudioRecorder()
    val context = LocalContext.current

    /** Settings **/
    val settingsStore = SettingsStore(context)
    val settingsViewModelFactory = remember {
        SettingsViewModelFactory(
            settingsStore = settingsStore
        )
    }
    val settingsViewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)

    /** AudioViewModel for control player and detect speech **/
    val classifier = EmotionClassifier().apply { this.loadClassifier(context) }
    val detector = SpeechDetector().apply { this.loadModel(context) }
    val audioViewModelFactory = remember {
        AudioViewModelFactory(
            audioProvider = audioRecorder,
            classifier = classifier,
            detector = detector,
            settingsStore = settingsStore
        )
    }
    val audioViewModel: AudioViewModel = viewModel<AudioViewModel>(
        factory = audioViewModelFactory
    ).apply { runTasks() }

    /** NotificationViewModel for notifications **/
    val notificationViewModelFactory = remember {
        NotificationViewModelFactory(
            audioProvider = audioRecorder,
            predictionRepository = PredictionRepository
        )
    }
    viewModel<NotificationViewModel>(factory = notificationViewModelFactory).apply { runTasks(context) }

    /** Timer **/
    val timerViewModel = viewModel<TimerViewModel>()

    /** StatisticsViewModel for statistics of audio **/
    val statisticsViewModel: StatisticsViewModel = viewModel<StatisticsViewModel>().apply {
        reset()
    }

    /** AlertViewModel for alerting **/
    val alertViewModelFactory = remember {
        AlertViewModelFactory(
            maxAngerDetectionTimeSeconds = settingsStore.angerDetectionTime,
            analyzePeriodSeconds = settingsStore.signalDetectionPeriod,
            phoneNumber = settingsStore.phoneNumber
        )
    }
    viewModel<AlertViewModel>(factory = alertViewModelFactory).apply { startListening() }

    /** Buttons state **/
    val isRecording = audioRecorder.isRunning().collectAsState()

    RecorderContent(
        audioProvider = audioRecorder,
        predictionProvider = statisticsViewModel,
        audioViewModel = audioViewModel,
        timerViewModel = timerViewModel,
        settingsViewModel = settingsViewModel,
        isRecording = isRecording
    )
}

@Composable
fun RecorderContent(
    audioProvider: AudioProvider,
    predictionProvider: PredictionProvider,
    audioViewModel: AudioViewModel,
    timerViewModel: TimerViewModel,
    settingsViewModel: SettingsViewModel,
    isRecording: State<Boolean>
) {
    Column(
        modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(horizontal = dimensionResource(R.dimen.prediction_view_padding)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /** Data **/
        val isDescriptionVisible: MutableState<Boolean> = remember { mutableStateOf(false) }
        val animatedWeight by animateFloatAsState(
            targetValue = if(isDescriptionVisible.value) 50f else 0f,
            animationSpec = tween(durationMillis = integerResource(R.integer.statistics_animation_ms))
        )
        val detectionPeriodSeconds = settingsViewModel.signalDetectionPeriod.collectAsState()
        val resetableObjects: List<Resetable> = listOf(
            timerViewModel, predictionProvider, audioViewModel
        )

        /** User interface **/
        if(50f - animatedWeight > 0f) {
            SpectrumWidget(
                modifier = Modifier
                    .weight(50f - animatedWeight),
                provider = audioViewModel,
                isRunning = isRecording
            )
        }

        TimerWidget(
            modifier = Modifier
                .weight(8f)
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(dimensionResource(R.dimen.component_timer_padding)),
            timerViewModel = timerViewModel,
            isRecording = isRecording
        )

        EvaluationWidget(
            modifier = Modifier
                .weight(20f)
                .background(MaterialTheme.colorScheme.secondary)
                .align(Alignment.CenterHorizontally)
                .padding(dimensionResource(R.dimen.prediction_description_padding)),
            predictionProvider = predictionProvider,
            detectionProvider = audioViewModel,
            detectionPeriodSeconds = detectionPeriodSeconds,
            isRecording = isRecording
        )

        Spacer(modifier = Modifier.weight(2f))

        SoundboardWidget(
            modifier = Modifier
                .fillMaxWidth()
                .weight(15f),
            mainButton = {
                StateButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    turnState = isRecording,
                    trueStateIconResource = R.drawable.mic_filled,
                    falseIconResource = R.drawable.stop_filled,
                    iconContentDescriptionResource = R.string.prediction_record_cd,
                    onClick = {
                        if (isRecording.value) audioProvider.stop()
                        else audioProvider.start()
                    }
                )
            },
            leftButton = {
                StateButton(
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .aspectRatio(1f),
                    turnState = isDescriptionVisible,
                    trueStateIconResource = R.drawable.arrow_up,
                    falseIconResource = R.drawable.arrow_down,
                    iconContentDescriptionResource = R.string.prediction_more_cd,
                    onClick = { isDescriptionVisible.value = !isDescriptionVisible.value }
                )
            },
            rightButton = {
                ClickButton(
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .aspectRatio(1f),
                    iconResource = R.drawable.refresh_filled,
                    iconContentDescriptionResource = R.string.prediction_refresh_cd,
                    onClick = {
                        resetableObjects.forEach(Resetable::reset)
                    }
                )
            }
        )

        Spacer(modifier = Modifier.weight(2f))

        if(animatedWeight > 0f) {
            EmotionStatisticsWidget(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(animatedWeight)
                    .padding(dimensionResource(R.dimen.component_statistics_padding)),
                detectionPeriodSeconds = detectionPeriodSeconds,
                predictionProvider = predictionProvider
            )
        }
    }
}

