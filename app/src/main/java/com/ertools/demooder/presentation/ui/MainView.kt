package com.ertools.demooder.presentation.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ertools.demooder.presentation.components.SideBar
import com.ertools.demooder.presentation.navigation.NavigationItem
import com.ertools.demooder.presentation.theme.Theme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Preview(name = "main", group = "main",
    device = "spec:id=reference_phone,shape=Normal,width=411,height=891,unit=dp,dpi=420",
    apiLevel = 34
)
@Composable
fun MainView () {
    Theme.MainTheme {
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = MaterialTheme.colorScheme.background
        )

        val navController = rememberNavController()
        val context = LocalContext.current
        SideBar(
            navController = navController,
            startRoute = NavigationItem.Home.route
        ) {
            composable(NavigationItem.Home.route) { HomeView(navController) }
            composable(NavigationItem.Prediction.route) { PredictionView(navController) }
            composable(NavigationItem.Records.route) { RecordsView(navController, context) }
            composable(NavigationItem.Settings.route) { SettingsView(navController) }
        }
    }
}
