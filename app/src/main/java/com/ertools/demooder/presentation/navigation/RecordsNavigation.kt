package com.ertools.demooder.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ertools.demooder.presentation.ui.PlayerView
import com.ertools.demooder.presentation.ui.RecordsView
import com.ertools.demooder.presentation.viewmodel.ProviderViewModel


@Composable
fun RecordsNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = OutsideNavigationItem.Main.route,
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
        composable(RecordsNavigationItem.RecordsList.route) { RecordsView(navController) }
        composable(RecordsNavigationItem.Player.route) { PlayerView() }
    }
}

sealed class RecordsNavigationItem(
    val route: String
) {
    data object RecordsList : RecordsNavigationItem(
        route = "RecordsList"
    )
    data object Player : RecordsNavigationItem(
        route = "Player"
    )
}