package com.ertools.demooder.presentation.components

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ertools.demooder.presentation.navigation.NavigationItem
import com.ertools.demooder.presentation.theme.COM_SIDEBAR_MARGIN
import com.ertools.demooder.presentation.ui.AudioVisualizer
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideBar(
    navController: NavHostController,
    context: Context
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    val navigationScreens = listOf(
        NavigationItem.Home,
        NavigationItem.Prediction,
        NavigationItem.Records,
        NavigationItem.Settings
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.background,
                drawerContentColor = MaterialTheme.colorScheme.onBackground,
            ) {
                Spacer(modifier = Modifier.height(COM_SIDEBAR_MARGIN))
                navigationScreens.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = index == selectedItemIndex,
                        onClick = {
                            navController.navigate(item.route)
                            selectedItemIndex = index
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) item.selectedIcon
                                else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        badge = { item.badgeCount?.let { Text(text = item.badgeCount.toString()) } },
                        modifier = Modifier.wrapContentWidth()
                    )
                }
            }
        },
        gesturesEnabled = true
    ) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        Scaffold(
            topBar = {
                AppBar(
                    onMenuClick = { scope.launch {
                        drawerState.apply { if(isClosed) open() else close() }
                    } },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { contentPadding ->
            NavHost(
                navController = navController,
                NavigationItem.Home.route,
                modifier = Modifier.padding(contentPadding)
            ) {
                composable(NavigationItem.Home.route) {
                    val views: List<Pair<String, @Composable () -> Unit>> = listOf(
                        "Emotion Recognizer" to { Text("Emotion Recognizer") },
                        "Audio Visualizer" to { AudioVisualizer(context) }
                    )
                    TabView(views)
                }
                composable(NavigationItem.Prediction.route) { AudioVisualizer(context) }

            }
        }
    }
}