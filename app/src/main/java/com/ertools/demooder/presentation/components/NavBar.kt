package com.ertools.demooder.presentation.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ertools.demooder.R
import com.ertools.demooder.presentation.navigation.InsideNavigationItem

@Composable
fun NavBar(
    selectedView: MutableState<InsideNavigationItem>,
    navigationItems: List<InsideNavigationItem>,
    navController: NavController,
) {
    val navigationItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
        unselectedIconColor = MaterialTheme.colorScheme.inversePrimary,
        disabledIconColor = MaterialTheme.colorScheme.tertiary,
        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
        unselectedTextColor = MaterialTheme.colorScheme.inversePrimary,
        disabledTextColor = MaterialTheme.colorScheme.tertiary,
        indicatorColor = Color.Transparent
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(
                0.01f * integerResource(R.integer.global_navbar_height_ratio)
            ),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        navigationItems.forEach { view ->
            NavigationBarItem(
                modifier = Modifier.wrapContentHeight(),
                label = {
                    Text(
                        text = (stringResource(view.title!!)),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                },
                alwaysShowLabel = true,
                selected = view.route == selectedView.value.route,
                icon = {
                    if(view.route == selectedView.value.route)
                        Icon(
                            painter = painterResource(view.selectedIcon!!),
                            contentDescription = view.route,
                            modifier = Modifier.size(32.dp)
                        )
                    else Icon(
                        painter = painterResource(view.unselectedIcon!!),
                        contentDescription = view.route,
                        modifier = Modifier.size(32.dp)
                    )
                },
                onClick = {
                    navController.navigate(view.route) {
                        popUpTo(selectedView.value.route) { inclusive = true }
                    }
                    selectedView.value = view
                },
                colors = navigationItemColors
            )
        }
    }
}