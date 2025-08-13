package com.ertools.demooder.presentation.ui

import android.util.Log
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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ertools.demooder.R
import com.ertools.demooder.core.audio.AudioPlayer
import com.ertools.demooder.core.audio.RecordingFile
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.demooder.core.settings.SettingsStore
import com.ertools.demooder.presentation.components.dialog.ClickButton
import com.ertools.demooder.presentation.components.dialog.StateButton
import com.ertools.demooder.presentation.components.widgets.EvaluationWidget
import com.ertools.demooder.presentation.components.widgets.SoundboardWidget
import com.ertools.demooder.presentation.components.widgets.SpectrumWidget
import com.ertools.demooder.presentation.viewmodel.AudioViewModel
import com.ertools.demooder.presentation.viewmodel.AudioViewModelFactory
import com.ertools.demooder.presentation.viewmodel.SettingsViewModel
import com.ertools.demooder.presentation.viewmodel.SettingsViewModelFactory
import com.ertools.demooder.presentation.viewmodel.StatisticsViewModel

@Composable
fun PlayerView(
    navController: NavHostController,
    recordingFile: RecordingFile?
) {
    val context = LocalContext.current
    val audioProvider = AudioPlayer(context, recordingFile!!)

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
            .padding(dimensionResource(R.dimen.prediction_view_padding)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpectrumWidget(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f),
            provider = audioViewModel,
            isRecording = isRecording
        )
        EvaluationWidget(
            modifier = Modifier
                .fillMaxHeight(0.45f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
                .align(Alignment.CenterHorizontally),
            predictionProvider = statisticsViewModel,
            detectionProvider = audioViewModel,
            detectionPeriodSeconds = detectionPeriodSeconds,
            isRecording = isRecording
        )

        SoundboardWidget(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            mainButton = {
                StateButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    state = isRecording,
                    enableIconResource = R.drawable.play_filled,
                    disableIconResource = R.drawable.pause_filled,
                    iconContentDescriptionResource = R.string.records_play_cd,
                    onClick = { audioViewModel.togglePlay(context) }
                )
            },
            leftButton = {
                ClickButton(
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .aspectRatio(1f),
                    iconResource = R.drawable.arrow_up,
                    iconContentDescriptionResource = R.string.records_more_cd,
                    onClick = { }
                )
            },
            rightButton = {
                ClickButton(
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .aspectRatio(1f),
                    iconResource = R.drawable.replay_filled,
                    iconContentDescriptionResource = R.string.records_replay_cd,
                    onClick = { audioViewModel.abort() }
                )
            }
        )
    }
}