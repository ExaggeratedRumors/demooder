package com.ertools.demooder.presentation.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ertools.demooder.presentation.components.AppScaffold
import com.ertools.demooder.presentation.navigation.NavigationItem
import com.ertools.demooder.presentation.theme.Theme
import com.ertools.demooder.presentation.viewmodel.MainViewModel

@Preview(name = "main", group = "main",
    device = "spec:id=reference_phone,shape=Normal,width=411,height=891,unit=dp,dpi=420",
    apiLevel = 34
)
@Composable
fun MainView () {
    val viewModel = viewModel<MainViewModel>()
    Theme.MainTheme {
        val context = LocalContext.current
        val navController = rememberNavController()
        AppScaffold(
            navController = navController,
        ) { contentPadding ->
            NavHost(
                navController = navController,
                startDestination = NavigationItem.Home.route,
                modifier = Modifier.padding(contentPadding),
                enterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    ExitTransition.None
                }
            ) {
                composable(NavigationItem.Home.route) { HomeView() }
                composable(NavigationItem.Prediction.route) { PredictionView(context) }
                composable(NavigationItem.Records.route) { RecordsView(viewModel) }
                composable(NavigationItem.Settings.route) { SettingsView(navController) }
            }
        }
    }
}
