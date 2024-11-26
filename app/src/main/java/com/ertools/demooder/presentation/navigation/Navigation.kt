package com.ertools.demooder.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ertools.demooder.R
import com.ertools.demooder.presentation.navigation.NavigationItem.Home
import com.ertools.demooder.presentation.navigation.NavigationItem.Prediction
import com.ertools.demooder.presentation.navigation.NavigationItem.Records
import com.ertools.demooder.presentation.navigation.NavigationItem.Settings

object NavigationItems {
    val list = listOf(Home, Prediction, Records, Settings)
}

sealed class NavigationItem(
    val route: String,
    @StringRes val title: Int? = null,
    @DrawableRes val selectedIcon: Int? = null,
    @DrawableRes val unselectedIcon: Int? = null,
    val badgeCount: Int? = null
) {
    data object Home : NavigationItem (
        route = "home",
        title = R.string.nav_home,
        selectedIcon = R.drawable.home_filled,
        unselectedIcon = R.drawable.home_outline
    )
    data object Prediction : NavigationItem (
        route = "prediction",
        title = R.string.nav_prediction,
        selectedIcon = R.drawable.predict_filled,
        unselectedIcon = R.drawable.predict_outline
    )
    data object Records : NavigationItem (
        route = "records",
        title = R.string.nav_records,
        selectedIcon = R.drawable.records_filled,
        unselectedIcon = R.drawable.records_outline,
        badgeCount = 0
    )
    data object Settings : NavigationItem (
        route = "settings",
        title = R.string.nav_settings,
        selectedIcon = R.drawable.settings_fileld,
        unselectedIcon = R.drawable.settings_outline
    )
}
