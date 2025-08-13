package com.ertools.demooder.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ertools.demooder.R
import com.ertools.demooder.presentation.components.shapes.PolygonShape
import com.ertools.demooder.presentation.theme.Shape

@Preview
@Composable
fun HomeView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .padding(vertical = dimensionResource(R.dimen.global_padding)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeHeader()
        HomeDescription()
        HomeContent()
    }
}

@Composable
fun ColumnScope.HomeHeader() {
    Box(
        modifier = Modifier
            .weight(0.35f)
            .aspectRatio(1f)
            .padding(dimensionResource(R.dimen.global_padding))
    ) {
        Image(
            painter = painterResource(id = R.drawable.vec_home_speech),
            contentDescription = "Speech bubble",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
                .clip(shape = MaterialTheme.shapes.large)
                .align(alignment = Alignment.Center)
        )
    }
}

@Composable
fun ColumnScope.HomeDescription() {
    val vertices = remember {
        listOf(
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.6f, 0.5f,
            0.8f, 1.0f,
            0.0f, 1.0f
        )
    }
    val rounding = remember {
        listOf(0f, 300f, 400f, 800f, 0f)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(0.35f)
            .clip(
                shape = PolygonShape(vertices, rounding)
            )
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        Text(stringResource(id = R.string.home_description_01))
        Spacer(Modifier.height(20.dp))
        Text(stringResource(id = R.string.home_description_02))
    }
}

@Composable
fun ColumnScope.HomeContent() {
    Column(
        modifier = Modifier
            .weight(0.3f),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.vec_home_emotion),
            contentDescription = "Speech bubble",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(0.6f)
                .clip(shape = MaterialTheme.shapes.large)
                .aspectRatio(1.5f)
                .align(alignment = Alignment.End)
        )
        val colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .weight(0.2f)
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally),
            shape = Shape.Shapes.extraSmall,
            colors = colors
        ) {
            Text(
                text = stringResource(id = R.string.home_button_speech),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
