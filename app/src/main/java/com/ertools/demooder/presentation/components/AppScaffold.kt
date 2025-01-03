package com.ertools.demooder.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ertools.demooder.presentation.navigation.NavigationItem
import com.ertools.demooder.presentation.navigation.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val navbarItems = listOf(
        NavigationItem.Home,
        NavigationItem.Prediction,
        NavigationItem.Records
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    /* Selected view */
    val navbarStack by navController.currentBackStackEntryAsState()
    val selectedView = remember { mutableStateOf(navbarItems[0]) }
    selectedView.value = NavigationRoutes.list.firstOrNull {
        it.route == navbarStack?.destination?.route
    } ?: NavigationItem.Home

    /* Nav bar */
    val showNavBar = rememberSaveable { mutableStateOf(true) }
    showNavBar.value = showNavBar(selectedView.value)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                selectedView = selectedView,
                scrollBehavior = scrollBehavior,
                menuItems = listOf(
                    MenuItem(
                        text = "Settings",
                        onClick = {
                            navController.navigate(NavigationItem.Settings.route)
                        }
                    )
                )
            )
        },
        bottomBar = {
            NavBar(
                selectedView = selectedView,
                navigationItems = navbarItems,
                navController = navController,
                showNavBar = showNavBar
            )
        }
    ) { contentPadding ->
        content(contentPadding)
    }
}

fun showNavBar(selectedView: NavigationItem): Boolean {
    return selectedView != NavigationItem.Settings
}