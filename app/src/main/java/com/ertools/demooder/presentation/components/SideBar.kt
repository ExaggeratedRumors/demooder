package com.ertools.demooder.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ertools.demooder.R
import com.ertools.demooder.presentation.navigation.NavigationItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideBar(
    navController: NavHostController,
    startRoute: String,
    content: NavGraphBuilder.() -> Unit
) {
    val navigationItemColors = NavigationDrawerItemDefaults.colors(
        selectedTextColor = MaterialTheme.colorScheme.primary,
        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
        selectedIconColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
        selectedContainerColor = Color.Transparent,
        unselectedContainerColor = Color.Transparent
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
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
                DrawerHeader()
                Spacer(modifier = Modifier.height(10.dp))
                navigationScreens.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(item.title!!),
                                style = MaterialTheme.typography.headlineLarge
                            )
                        },
                        selected = index == selectedItemIndex,
                        onClick = {
                            navController.navigate(item.route)
                            selectedItemIndex = index
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex)
                                    ImageVector.vectorResource(item.selectedIcon!!)
                                else
                                    ImageVector.vectorResource(item.unselectedIcon!!),
                                contentDescription = stringResource(item.title!!)
                            )
                        },
                        colors = navigationItemColors,
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
                    onMoreClick = { scope.launch {
                        drawerState.apply { if(isClosed) open() else close() }
                    } },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { contentPadding ->
            NavHost(
                navController = navController,
                startDestination = startRoute,
                modifier = Modifier.padding(contentPadding)
            ) {
                content()
               /* composable(NavigationItem.Home.route) {
                    val views: List<Pair<String, @Composable () -> Unit>> = listOf(
                        "Emotion Recognizer" to { Text("Emotion Recognizer") },
                        "Audio Visualizer" to { AudioVisualizer(context) }
                    )
                    TabView(views)
                }
                composable(NavigationItem.Prediction.route) { AudioVisualizer(context) }*/

            }
        }
    }
}

@Composable
fun DrawerHeader() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                color = MaterialTheme.colorScheme.primary
            )
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 10.dp)
                .align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Icon(
            contentDescription = "User icon",
            imageVector = ImageVector.vectorResource(R.drawable.settings_fileld),
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(64.dp)
        )
        Text(
            text = "Placeholder name",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}