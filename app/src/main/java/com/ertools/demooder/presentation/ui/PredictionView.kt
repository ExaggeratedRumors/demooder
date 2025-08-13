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
import com.ertools.demooder.presentation.viewmodel.ProviderViewModel
import com.ertools.demooder.presentation.viewmodel.SettingsViewModel
import com.ertools.demooder.presentation.viewmodel.SettingsViewModelFactory
import com.ertools.demooder.presentation.viewmodel.StatisticsViewModel

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
                    enableIconResource = R.drawable.mic_filled,
                    disableIconResource = R.drawable.stop_filled,
                    iconContentDescriptionResource = R.string.prediction_record_cd,
                    onClick = { audioViewModel.togglePlay(context) }
                )
            },
            leftButton = {
                ClickButton(
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .aspectRatio(1f),
                    iconResource = R.drawable.settings_link,
                    iconContentDescriptionResource = R.string.prediction_save_cd,
                    onClick = { audioViewModel.more() }
                )
            },
            rightButton = {
                ClickButton(
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .aspectRatio(1f),
                    iconResource = R.drawable.records_filled,
                    iconContentDescriptionResource = R.string.prediction_clear_cd,
                    onClick = { audioViewModel.abort() }
                )
            }
        )
    }
}

