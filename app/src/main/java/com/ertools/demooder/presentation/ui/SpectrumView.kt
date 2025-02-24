package com.ertools.demooder.presentation.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.unit.dp
import com.ertools.demooder.R
import com.ertools.demooder.core.recorder.AudioRecorder
import com.ertools.demooder.core.recorder.SpectrumProvider
import com.ertools.demooder.utils.AppConstants
import kotlinx.coroutines.delay

@Composable
fun SpectrumView(
    modifier: Modifier = Modifier,
    recorder: AudioRecorder
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ){
        SpectrumGraph(provider = recorder)
    }
}

@Composable
fun SpectrumGraph (provider: SpectrumProvider) {
    var spectrum by remember { mutableStateOf(provider.getOctavesAmplitudeSpectrum()) }

    LaunchedEffect(key1 = true) {
        while (true) {
            spectrum = provider.getOctavesAmplitudeSpectrum()
            delay(AppConstants.UI_GRAPH_UPDATE_DELAY)
        }
    }

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