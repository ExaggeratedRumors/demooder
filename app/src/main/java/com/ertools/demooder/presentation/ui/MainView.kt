package com.ertools.demooder.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
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
        val navController = rememberNavController()
        val context = LocalContext.current
        AppScaffold(
            listOf(
                NavigationItem.Home,
                NavigationItem.Prediction,
                NavigationItem.Records
            ),
            startRoute = NavigationItem.Home.route
        ) {
            composable(NavigationItem.Home.route) { HomeView(navController) }
            composable(NavigationItem.Prediction.route) { PredictionView(navController, context) }
            composable(NavigationItem.Records.route) { RecordsView(navController, viewModel) }
            composable(NavigationItem.Settings.route) { SettingsView(navController) }
        }
    }
}
