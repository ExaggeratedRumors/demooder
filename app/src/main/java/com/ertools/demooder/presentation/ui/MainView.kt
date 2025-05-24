package com.ertools.demooder.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ertools.demooder.presentation.components.AppScaffold
import com.ertools.demooder.presentation.navigation.InsideNavigation

@Preview(name = "main", group = "main",
    device = "spec:width=411dp,height=891dp,dpi=420",
    apiLevel = 34
)
@Composable
fun MainViewPreview() {
    MainView(rememberNavController())
}

@Composable
fun MainView (parentNavController: NavController) {
    val navController = rememberNavController()
    AppScaffold(
        navController = navController,
        parentNavController = parentNavController
    ) { contentPadding ->
        InsideNavigation(
            modifier = Modifier.padding(contentPadding),
            navController = navController
        )
    }
}
