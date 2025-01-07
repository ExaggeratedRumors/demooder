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
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ertools.demooder.presentation.navigation.AppNavigationItem
import com.ertools.demooder.presentation.navigation.ScaffoldNavigationItem
import com.ertools.demooder.presentation.navigation.ScaffoldNavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: NavController,
    parentNavController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    val navbarItems = ScaffoldNavigationRoutes.list

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    /* Selected view */
    val navbarStack by navController.currentBackStackEntryAsState()
    val selectedView = remember { mutableStateOf(navbarItems[0]) }
    selectedView.value = ScaffoldNavigationRoutes.list.firstOrNull {
        it.route == navbarStack?.destination?.route
    } ?: ScaffoldNavigationItem.Home

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
                            parentNavController.navigate(AppNavigationItem.Settings.route)
                        }
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
        content(contentPadding)
    }
}