package com.ertools.demooder.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ertools.demooder.R
import com.ertools.demooder.presentation.ui.HomeView
import com.ertools.demooder.presentation.ui.PredictionView
import com.ertools.demooder.presentation.viewmodel.ProviderViewModel


@Composable
fun InsideNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val providerViewModel = viewModel<ProviderViewModel>()
    NavHost(
        navController = navController,
        startDestination = InsideNavigationItem.Home.route,
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
        composable(InsideNavigationItem.Home.route) { HomeView() }
        composable(InsideNavigationItem.Prediction.route) { PredictionView(providerViewModel) }
        composable(InsideNavigationItem.Records.route) {
            val recordsNavController = rememberNavController()
            RecordsNavigation(
                modifier = Modifier.padding(dimensionResource(R.dimen.records_list_padding)),
                navController = recordsNavController
            )
        }
    }
}

object InsideNavigationRoutes {
    val list = listOf(
        InsideNavigationItem.Home,
        InsideNavigationItem.Prediction,
        InsideNavigationItem.Records
    )
}

sealed class InsideNavigationItem(
    val route: String,
    @StringRes val title: Int? = null,
    @DrawableRes val selectedIcon: Int? = null,
    @DrawableRes val unselectedIcon: Int? = null,
) {
    data object Home : InsideNavigationItem(
        route = "home",
        title = R.string.nav_home,
        selectedIcon = R.drawable.home_filled,
        unselectedIcon = R.drawable.home_outline
    )

    data object Prediction : InsideNavigationItem(
        route = "prediction",
        title = R.string.nav_prediction,
        selectedIcon = R.drawable.predict_filled,
        unselectedIcon = R.drawable.predict_outline
    )

    data object Records : InsideNavigationItem(
        route = "records",
        title = R.string.nav_records,
        selectedIcon = R.drawable.records_filled,
        unselectedIcon = R.drawable.records_outline
    )
}

