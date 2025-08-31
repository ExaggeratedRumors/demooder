package com.ertools.demooder.presentation.components.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import com.ertools.demooder.R
import com.ertools.demooder.presentation.viewmodel.StatisticsViewModel
import com.ertools.processing.commons.Emotion

@Composable
fun StatisticsWidget(
    modifier: Modifier = Modifier,
    statisticsViewModel: StatisticsViewModel,
    isVisible: Boolean
) {
    val density = LocalDensity.current
    val emotionsState = Emotion.entries.associateWith {
        statisticsViewModel.getEmotionFlow(it).collectAsState()
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 500),
            initialOffsetY = { fullHeight -> fullHeight }
        ),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 500),
            targetOffsetY = { fullHeight -> fullHeight }
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.component_card_padding)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                emotionsState.forEach { emotion ->
                    TitleValueWidget(
                        modifier = Modifier,
                        title = emotion.key.name,
                        value = "${emotion.value.value}s",
                        isVertical = false
                    )
                }
            }
        }
    }
}