package com.ertools.demooder.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ertools.demooder.presentation.components.ReturnScaffold

@Composable
fun OptionView(
    optionTitle: String,
    currentValue: String,
    navController: NavController,
    validation: (String) -> Boolean,
    onSave: () -> Unit
) {
    ReturnScaffold(
        viewName = optionTitle,
        onReturn = { navController.popBackStack() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Option title: $optionTitle"
            )
            Text(text = "Current value: $currentValue")
        }
    }
}