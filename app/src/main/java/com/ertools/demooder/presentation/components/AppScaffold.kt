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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ertools.demooder.presentation.navigation.NavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navigationItems: List<NavigationItem>,
    startRoute: String,
    content: NavGraphBuilder.() -> Unit
) {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val selectedView = remember { mutableStateOf(navigationItems[0]) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                selectedView = selectedView,
                onMoreClick = { },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            NavBar(
                selectedView = selectedView,
                navigationItems = navigationItems,
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