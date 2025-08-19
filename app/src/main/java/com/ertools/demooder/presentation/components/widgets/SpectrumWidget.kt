package com.ertools.demooder.presentation.components.widgets

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ertools.demooder.R
import com.ertools.demooder.core.spectrum.SpectrumProvider


/**
 * Displays the spectrum of the audio signal.
 * @param modifier Modifier to be applied to the view.
 * @param provider SpectrumProvider giving information about the audio spectrum.
 * @param isRunning State indicating if the recorder is currently recording.
 */
@Composable
fun SpectrumWidget(
    modifier: Modifier = Modifier,
    provider: SpectrumProvider,
    isRunning: State<Boolean>
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
                .height(dimensionResource(R.dimen.prediction_equalizer_height))
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(dimensionResource(R.dimen.prediction_spectrum_padding)),
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
                            .padding(dimensionResource(R.dimen.prediction_spectrum_padding))
                            .fillMaxHeight()
                            .align(Alignment.Bottom)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                    ) {
                        if (isRunning.value) {
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
        }
    }
}
