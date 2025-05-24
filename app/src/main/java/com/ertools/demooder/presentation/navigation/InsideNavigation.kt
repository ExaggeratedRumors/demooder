package com.ertools.demooder.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ertools.demooder.R
import com.ertools.demooder.core.audio.AudioProvider
import com.ertools.demooder.presentation.ui.HomeView
import com.ertools.demooder.presentation.ui.PredictionView
import com.ertools.demooder.presentation.ui.RecordsView


@Composable
fun ScaffoldNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ScaffoldNavigationItem.Home.route,
        modifier = modifier,
        enterTransition = {
            fadeIn(
                animationSpec = tween(100)
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(100)
            )
        }
    ) {
        composable(ScaffoldNavigationItem.Home.route) { HomeView() }
        composable() {
            PredictionView()
        }
        composable(ScaffoldNavigationItem.Records.route) { RecordsView(navController) }
    }
}

object ScaffoldNavigationRoutes {
    val list = listOf(
        ScaffoldNavigationItem.Home,
        ScaffoldNavigationItem.Prediction,
        ScaffoldNavigationItem.Records
    )
}

sealed class ScaffoldNavigationItem(
    val route: String,
    @StringRes val title: Int? = null,
    @DrawableRes val selectedIcon: Int? = null,
    @DrawableRes val unselectedIcon: Int? = null,
    val badgeCount: Int? = null
) {
    data object Home : ScaffoldNavigationItem(
        route = "home",
        title = R.string.nav_home,
        selectedIcon = R.drawable.home_filled,
        unselectedIcon = R.drawable.home_outline
    )

    data object Prediction : ScaffoldNavigationItem(
        route = "prediction",
        title = R.string.nav_prediction,
        selectedIcon = R.drawable.predict_filled,
        unselectedIcon = R.drawable.predict_outline
    )

    data object Records : ScaffoldNavigationItem(
        route = "records",
        title = R.string.nav_records,
        selectedIcon = R.drawable.records_filled,
        unselectedIcon = R.drawable.records_outline,
        badgeCount = 0
    )
}

