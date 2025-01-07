package com.ertools.demooder.presentation.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ertools.demooder.presentation.components.TabLayout
import com.ertools.demooder.presentation.viewmodel.MainViewModel

@Composable
fun RecordsView() {
    val viewModel = viewModel<MainViewModel>()
    Column(modifier = Modifier.fillMaxSize().draggable(
        state = viewModel.dragState.value!!,
        orientation = Orientation.Horizontal,
        onDragStarted = {  },
        onDragStopped = {
            viewModel.updateTabIndexBasedOnSwipe()
        }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabLayout(
            views = listOf(
                "internal" to { InternalRecordsView() },
                "external" to { ExternalRecordsView() }
            )
        )
    }
}

@Composable
fun ColumnScope.InternalRecordsView() {
    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
        Text(
            text = "From Internal",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ColumnScope.ExternalRecordsView() {
    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
        Text(
            text = "From External",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}