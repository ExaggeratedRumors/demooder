package com.ertools.demooder.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ertools.demooder.presentation.navigation.NavigationItem
import com.ertools.demooder.presentation.navigation.NavigationItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: NavHostController,
    startRoute: String,
    content: NavGraphBuilder.() -> Unit
) {
    val navbarItems = listOf(
        NavigationItem.Home,
        NavigationItem.Prediction,
        NavigationItem.Records
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val selectedView = remember { mutableStateOf(navbarItems[0]) }
    val navbarStack by navController.currentBackStackEntryAsState()
    selectedView.value = NavigationItems.list.firstOrNull {
        it.route == navbarStack?.destination?.route
    } ?: NavigationItem.Home

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                selectedView = selectedView,
                scrollBehavior = scrollBehavior,
                menuItems = listOf(
                    MenuItem(
                        text = "Settings",
                        onClick = { navController.navigate(NavigationItem.Settings.route) }
                    )
                )
            )
        },
        bottomBar = {
            NavBar(
                selectedView = selectedView,
                navigationItems = navbarItems,
                navController = navController
            )
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier.padding(contentPadding)
        ) {
            content()
        }
    }
}