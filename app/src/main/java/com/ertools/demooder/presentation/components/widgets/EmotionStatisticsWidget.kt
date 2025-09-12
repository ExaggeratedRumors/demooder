package com.ertools.demooder.presentation.components.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ertools.demooder.R
import com.ertools.demooder.core.classifier.PredictionProvider
import com.ertools.demooder.utils.Translations
import com.ertools.processing.commons.Emotion

/**
 * A widget that displays statistics for each emotion based on the provided [PredictionProvider].
 *
 * @param modifier The modifier to be applied to the widget.
 * @param predictionProvider The provider that supplies prediction data for calculating statistics.
 */
@Composable
fun EmotionStatisticsWidget(
    modifier: Modifier = Modifier,
    predictionProvider: PredictionProvider
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Emotion.entries.forEach { emotion ->
                    val emotionTranslation = stringResource(
                        Translations.emotions[emotion.name]?: R.string.empty
                    )
                    val emotionFlow = predictionProvider.count(emotion).collectAsState()

                    TitleValueWidget(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.component_statistics_element_padding)),
                        title = "${emotion.name} ($emotionTranslation)",
                        value = "${emotionFlow.value}s",
                        isVertical = false
                    )
                    if(emotion != Emotion.entries.last()) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = dimensionResource(R.dimen.global_divider_thickness),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}