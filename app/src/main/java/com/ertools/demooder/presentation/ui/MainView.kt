package com.ertools.demooder.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ertools.demooder.presentation.components.AppScaffold
import com.ertools.demooder.presentation.navigation.ScaffoldNavigation
import com.ertools.demooder.presentation.theme.Theme

@Preview(name = "main", group = "main",
    device = "spec:id=reference_phone,shape=Normal,width=411,height=891,unit=dp,dpi=420",
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
        ScaffoldNavigation(
            modifier = Modifier.padding(contentPadding),
            navController = navController
        )
    }
}
