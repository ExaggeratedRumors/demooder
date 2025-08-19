package com.ertools.demooder.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ertools.demooder.R
import com.ertools.demooder.core.audio.AudioRecorder
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.classifier.PredictionRepository
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.demooder.core.settings.SettingsStore
import com.ertools.demooder.presentation.components.dialog.ClickButton
import com.ertools.demooder.presentation.components.dialog.StateButton
import com.ertools.demooder.presentation.components.widgets.EvaluationWidget
import com.ertools.demooder.presentation.components.widgets.SoundboardWidget
import com.ertools.demooder.presentation.components.widgets.SpectrumWidget
import com.ertools.demooder.presentation.viewmodel.AudioViewModel
import com.ertools.demooder.presentation.viewmodel.AudioViewModelFactory
import com.ertools.demooder.presentation.viewmodel.NotificationViewModel
import com.ertools.demooder.presentation.viewmodel.NotificationViewModelFactory
import com.ertools.demooder.presentation.viewmodel.SettingsViewModel
import com.ertools.demooder.presentation.viewmodel.SettingsViewModelFactory
import com.ertools.demooder.presentation.viewmodel.StatisticsViewModel

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
    val detectionPeriodSeconds = settingsViewModel.signalDetectionPeriod.collectAsState()

    /** Recorder **/
    val classifier = EmotionClassifier().apply { this.loadClassifier(context) }
    val detector = SpeechDetector().apply { this.loadModel(context) }
    val recorderViewModelFactory = remember {
        AudioViewModelFactory(
            audioProvider = audioRecorder,
            classifier = classifier,
            detector = detector,
            settingsStore = settingsStore
        )
    }
    val audioViewModel: AudioViewModel = viewModel<AudioViewModel>(factory = recorderViewModelFactory).apply {
        runTasks()
    }

    /** Notifications **/
    val notificationViewModelFactory = remember {
        NotificationViewModelFactory(
            audioProvider = audioRecorder,
            predictionRepository = PredictionRepository
        )
    }
    val notificationViewModel: NotificationViewModel = viewModel<NotificationViewModel>(
        factory = notificationViewModelFactory
    ).apply { runTasks(context) }

    /** Statistics **/
    val statisticsViewModel: StatisticsViewModel = viewModel()

    /** Buttons state **/
    val isRecording = audioViewModel.isWorking.collectAsState()
    val isDescriptionVisible = statisticsViewModel.isDescriptionVisible.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(dimensionResource(R.dimen.prediction_view_padding)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpectrumWidget(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            provider = audioViewModel,
            isRunning = isRecording
        )
        EvaluationWidget(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .background(MaterialTheme.colorScheme.secondary)
                .align(Alignment.CenterHorizontally)
                .padding(dimensionResource(R.dimen.prediction_description_padding)),
            predictionProvider = statisticsViewModel,
            detectionProvider = audioViewModel,
            detectionPeriodSeconds = detectionPeriodSeconds,
            isRecording = isRecording
        )

        SoundboardWidget(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
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
                        if (isRecording.value) audioRecorder.stop()
                        else audioRecorder.start()
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
                    onClick = {  }
                )
            },
            rightButton = {
                ClickButton(
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .aspectRatio(1f),
                    iconResource = R.drawable.refresh_filled,
                    iconContentDescriptionResource = R.string.prediction_refresh_cd,
                    onClick = {  }
                )
            }
        )
    }
}

