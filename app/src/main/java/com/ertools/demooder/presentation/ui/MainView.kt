package com.ertools.demooder.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ertools.demooder.presentation.components.SideBar
import com.ertools.demooder.presentation.navigation.NavigationItem
import com.ertools.demooder.presentation.theme.Theme

@Preview(name = "main", group = "main",
    device = "spec:id=reference_phone,shape=Normal,width=411,height=891,unit=dp,dpi=420",
    apiLevel = 33
)
@Composable
fun MainView () {
    Theme.MainTheme {
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
