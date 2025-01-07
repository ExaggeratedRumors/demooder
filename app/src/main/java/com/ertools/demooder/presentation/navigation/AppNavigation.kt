package com.ertools.demooder.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ertools.demooder.presentation.ui.MainView
import com.ertools.demooder.presentation.ui.OptionView
import com.ertools.demooder.presentation.ui.SettingsView

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppNavigationItem.Main.route,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(500)
            )
        }
    ) {
        composable(AppNavigationItem.Main.route) { MainView(navController) }
        composable(AppNavigationItem.Settings.route) { SettingsView(navController) }
    }
}

sealed class AppNavigationItem(
    val route: String
) {
    data object Main : AppNavigationItem(
        route = "main"
    )
    data object Settings : AppNavigationItem(
        route = "settings"
    )
}