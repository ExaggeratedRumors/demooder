package com.ertools.demooder.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import com.ertools.demooder.R
import com.ertools.demooder.core.audio.AudioPlayer
import com.ertools.demooder.core.audio.RecordingFile
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.demooder.core.settings.SettingsStore
import com.ertools.demooder.presentation.components.dialog.ClickButton
import com.ertools.demooder.presentation.components.dialog.StateButton
import com.ertools.demooder.presentation.components.widgets.AudioSeekBarWidget
import com.ertools.demooder.presentation.components.widgets.EvaluationWidget
import com.ertools.demooder.presentation.components.widgets.SoundboardWidget
import com.ertools.demooder.presentation.components.widgets.SpectrumWidget
import com.ertools.demooder.presentation.components.widgets.TitleValue
import com.ertools.demooder.presentation.viewmodel.AudioViewModel
import com.ertools.demooder.presentation.viewmodel.AudioViewModelFactory
import com.ertools.demooder.presentation.viewmodel.SeekBarViewModel
import com.ertools.demooder.presentation.viewmodel.SeekBarViewModelFactory
import com.ertools.demooder.presentation.viewmodel.SettingsViewModel
import com.ertools.demooder.presentation.viewmodel.SettingsViewModelFactory
import com.ertools.demooder.presentation.viewmodel.StatisticsViewModel

@Composable
fun PlayerView(
    recordingFile: RecordingFile?
) {
    val context = LocalContext.current
    val audioPlayer = AudioPlayer(context, recordingFile!!)

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

    /** AudioViewModel for control player and detect speech **/
    val audioViewModelFactory = remember {
        AudioViewModelFactory(
            audioProvider = audioPlayer,
            classifier = classifier,
            detector = detector,
            settingsStore = settingsStore
        )
    }
    val audioViewModel: AudioViewModel = viewModel<AudioViewModel>(factory = audioViewModelFactory).apply {
        runNotificationListeningTask(context)
    }

    /** SeekBarViewModel for control seek bar **/
    val seekBarViewModelFactory = remember {
        SeekBarViewModelFactory(
            progressProvider = audioPlayer
        )
    }
    val seekBarViewModel: SeekBarViewModel = viewModel<SeekBarViewModel>(factory = seekBarViewModelFactory)

    /** StatisticsViewModel for statistics of audio **/
    val statisticsViewModel: StatisticsViewModel = viewModel()

    /** Buttons state **/
    val isRecording = audioViewModel.isWorking.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(dimensionResource(R.dimen.records_view_padding)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpectrumWidget(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.375f),
            provider = audioViewModel,
            isRecording = isRecording
        )

        AudioSeekBarWidget(
            modifier = Modifier.fillMaxHeight(0.2f).fillMaxWidth(),
            durationStateFlow = seekBarViewModel.duration,
            positionStateFlow = seekBarViewModel.position,
            onSeekChange = { seekBarViewModel.seekTo(it) }
        )

        Column(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
                .padding(dimensionResource(R.dimen.records_description_padding))
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TitleValue(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.records_description_padding))
                    .align(Alignment.CenterHorizontally),
                title = stringResource(R.string.records_recording_label),
                value = recordingFile.name
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(0.8f),
                thickness = dimensionResource(R.dimen.records_divider_thickness),
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f)
            )
            EvaluationWidget(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                predictionProvider = statisticsViewModel,
                detectionProvider = audioViewModel,
                detectionPeriodSeconds = detectionPeriodSeconds,
                isRecording = isRecording
            )
        }

        SoundboardWidget(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f),
            mainButton = {
                StateButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    state = isRecording,
                    enableIconResource = R.drawable.play_filled,
                    disableIconResource = R.drawable.pause_filled,
                    iconContentDescriptionResource = R.string.records_play_cd,
                    onClick = {
                        audioViewModel.togglePlay(context)
                        seekBarViewModel.togglePlay()

                    }
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


