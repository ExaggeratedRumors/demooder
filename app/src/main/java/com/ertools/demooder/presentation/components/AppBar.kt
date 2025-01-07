package com.ertools.demooder.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ertools.demooder.R
import com.ertools.demooder.presentation.navigation.ScaffoldNavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    selectedView: MutableState<ScaffoldNavigationItem>,
    scrollBehavior: TopAppBarScrollBehavior,
    menuItems: List<MenuItem>
) {
    val menuExpanded = remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = stringResource(selectedView.value.title!!),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        actions = {
            IconButton(onClick = { menuExpanded.value = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            DropdownMenu(
                expanded = menuExpanded.value,
                onDismissRequest = { menuExpanded.value = false },
                containerColor = MaterialTheme.colorScheme.background,
                border = BorderStroke(
                    dimensionResource(id = R.dimen.global_dropdown_corner),
                    MaterialTheme.colorScheme.background
                ),
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                shadowElevation = 0.dp,
                tonalElevation = 0.dp
            ) {
                menuItems.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(text = item.text)
                        },
                        onClick = { 
                            menuExpanded.value = false
                            item.onClick(Unit) 
                        },
                        modifier = Modifier.fillMaxHeight(),
                        colors = MenuItemColors(
                            textColor = MaterialTheme.colorScheme.onSurface,
                            leadingIconColor = MaterialTheme.colorScheme.onSurface,
                            trailingIconColor = MaterialTheme.colorScheme.onSurface,
                            disabledTextColor = MaterialTheme.colorScheme.onTertiary,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onTertiary,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onTertiary
                        )

                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

data class MenuItem(
    val text: String,
    val onClick: (Unit) -> (Unit)
)