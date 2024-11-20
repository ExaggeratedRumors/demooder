package com.ertools.demooder.presentation.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ertools.demooder.presentation.navigation.NavigationItem

@Composable
fun BottomNavBar(
    navigationItems: List<NavigationItem>,
    startRoute: String,
    content: NavGraphBuilder.() -> Unit
) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }

    val navigationItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
        disabledIconColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        indicatorColor = Color.Transparent
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                navigationItems.forEachIndexed { index, view ->
                    NavigationBarItem(
                        modifier = Modifier.wrapContentHeight(),
                        label = { "XD$index" },
                        alwaysShowLabel = true,
                        selected = index == selectedItem,
                        icon = {
                            val c = ImageVector.vectorResource(view.selectedIcon!!)
                            //else Icons.Outlined.Home/*ImageVector.vectorResource(view.unselectedIcon!!)*/
                        },
                        onClick = {
                            selectedItem = index
                            navController.navigate(view.route)
                        },
                        colors = NavigationBarItemDefaults.colors()
                    )
                }
            }
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