package com.ertools.demooder.presentation.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun PredictionView(
    navController: NavHostController,
    context: Context
) {
    AudioVisualizer(context)
}