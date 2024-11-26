package com.ertools.demooder.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ertools.demooder.R
import com.ertools.demooder.presentation.components.PolygonShape

@Preview
@Composable
fun HomeView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeHeader()
        HomeDescription()
    }
}

@Composable
fun HomeHeader() {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.3f)
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
fun HomeDescription() {
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
            .fillMaxHeight(0.4f)
            .clip(
                shape = PolygonShape(vertices, rounding)
            )
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Demooder")
        Spacer(Modifier.height(20.dp))
    }
}


