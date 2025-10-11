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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ertools.demooder.R
import com.ertools.demooder.core.audio.AudioPlayer
import com.ertools.demooder.core.audio.AudioProvider
import com.ertools.demooder.core.audio.RecordingFile
import com.ertools.demooder.core.classifier.EmotionClassifier
import com.ertools.demooder.core.classifier.PredictionProvider
import com.ertools.demooder.core.classifier.PredictionRepository
import com.ertools.demooder.core.detector.SpeechDetector
import com.ertools.demooder.core.settings.SettingsStore
import com.ertools.demooder.presentation.components.dialog.ClickButton
import com.ertools.demooder.presentation.components.dialog.StateButton
import com.ertools.demooder.presentation.components.interfaces.Resetable
import com.ertools.demooder.presentation.components.widgets.AudioSeekBarWidget
import com.ertools.demooder.presentation.components.widgets.EmotionStatisticsWidget
import com.ertools.demooder.presentation.components.widgets.EvaluationWidget
import com.ertools.demooder.presentation.components.widgets.SoundboardWidget
import com.ertools.demooder.presentation.components.widgets.SpectrumWidget
import com.ertools.demooder.presentation.components.widgets.TitleValueWidget
import com.ertools.demooder.presentation.viewmodel.AudioViewModel
import com.ertools.demooder.presentation.viewmodel.AudioViewModelFactory
import com.ertools.demooder.presentation.viewmodel.NotificationViewModel
import com.ertools.demooder.presentation.viewmodel.NotificationViewModelFactory
import com.ertools.demooder.presentation.viewmodel.SeekBarViewModel
import com.ertools.demooder.presentation.viewmodel.SeekBarViewModelFactory
import com.ertools.demooder.presentation.viewmodel.SettingsViewModel
import com.ertools.demooder.presentation.viewmodel.SettingsViewModelFactory
import com.ertools.demooder.presentation.viewmodel.StatisticsViewModel
import com.ertools.demooder.presentation.viewmodel.TimerViewModel
import kotlinx.coroutines.delay

@Composable
fun PlayerView(
    recordingFile: RecordingFile?
) {
    val isAudioCompleted: MutableState<Boolean> = remember { mutableStateOf(false) }
    val context = LocalContext.current

    /** Audio Player **/
    val audioPlayer = AudioPlayer(
        context,
        recordingFile!!,
        onStopCallback = {
            isAudioCompleted.value = true
            Log.d("PlayerView", "Audio playback completed for file: ${recordingFile.name}")
        }
    )

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
            audioProvider = audioPlayer,
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
            audioProvider = audioPlayer,
            predictionRepository = PredictionRepository
        )
    }
    viewModel<NotificationViewModel>(factory = notificationViewModelFactory).apply { runTasks(context) }

    /** SeekBarViewModel for control seek bar **/
    val seekBarViewModelFactory = remember {
        SeekBarViewModelFactory(
            progressProvider = audioPlayer,
            audioProvider = audioPlayer
        )
    }
    val seekBarViewModel: SeekBarViewModel = viewModel<SeekBarViewModel>(
        factory = seekBarViewModelFactory
    ).apply { runTasks() }

    /** StatisticsViewModel for statistics of audio **/
    val statisticsViewModel: StatisticsViewModel = viewModel()

    /** Buttons state **/
    val isPlaying = audioPlayer.isRunning().collectAsState()

    LaunchedEffect(isAudioCompleted) {
        while(true) {
            if (isAudioCompleted.value) {
                Log.d("PlayerView", "Stopping audio player as playback is completed.")
                audioPlayer.stop()
                isAudioCompleted.value = false
            }
            delay(500)
        }
    }

    PlayerContent(
        audioProvider = audioPlayer,
        predictionProvider = statisticsViewModel,
        audioViewModel = audioViewModel,
        settingsViewModel = settingsViewModel,
        seekBarViewModel = seekBarViewModel,
        recordingFile = recordingFile,
        isPlaying = isPlaying
    )
}

@Composable
fun PlayerContent(
    audioProvider: AudioProvider,
    predictionProvider: PredictionProvider,
    audioViewModel: AudioViewModel,
    settingsViewModel: SettingsViewModel,
    seekBarViewModel: SeekBarViewModel,
    recordingFile: RecordingFile,
    isPlaying: State<Boolean>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = dimensionResource(R.dimen.records_player_padding)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /** Data **/
        val descriptionWeight = 35f
        val isDescriptionVisible: MutableState<Boolean> = remember { mutableStateOf(false) }
        val animatedWeight by animateFloatAsState(
            targetValue = if(isDescriptionVisible.value) descriptionWeight else 0f,
            animationSpec = tween(durationMillis = integerResource(R.integer.statistics_animation_ms))
        )
        val detectionPeriodSeconds = settingsViewModel.signalDetectionPeriod.collectAsState()

        /** User interface **/
        if(descriptionWeight - animatedWeight > 0f) {
            SpectrumWidget(
                modifier = Modifier
                    .weight(descriptionWeight - animatedWeight),
                provider = audioViewModel,
                isRunning = isPlaying
            )
        }

        AudioSeekBarWidget(
            modifier = Modifier
                .weight(13f)
                .fillMaxWidth(),
            durationStateFlow = seekBarViewModel.duration,
            positionStateFlow = seekBarViewModel.position,
            onSeekChange = { seekBarViewModel.seekTo(it) }
        )

        Column(
            modifier = Modifier
                .weight(30f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
                .padding(dimensionResource(R.dimen.records_description_padding))
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TitleValueWidget(
                modifier = Modifier
                    .weight(10f)
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
                modifier = Modifier
                    .weight(20f)
                    .fillMaxWidth(),
                predictionProvider = predictionProvider,
                detectionProvider = audioViewModel,
                detectionPeriodSeconds = detectionPeriodSeconds,
                isRecording = isPlaying
            )
        }

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
                    turnState = isPlaying,
                    trueStateIconResource = R.drawable.play_filled,
                    falseIconResource = R.drawable.pause_filled,
                    iconContentDescriptionResource = R.string.records_play_cd,
                    onClick = {
                        if(isPlaying.value) audioProvider.stop()
                        else audioProvider.start()
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
                    onClick = { isDescriptionVisible.value = !isDescriptionVisible.value }
                )
            },
            rightButton = {
                ClickButton(
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .aspectRatio(1f),
                    iconResource = R.drawable.replay_filled,
                    iconContentDescriptionResource = R.string.records_replay_cd,
                    onClick = { seekBarViewModel.seekTo(0) }
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
                predictionProvider = predictionProvider
            )
        }
    }
}

