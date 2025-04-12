package com.ertools.demooder.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ertools.demooder.R
import com.ertools.demooder.presentation.components.OptionData
import com.ertools.demooder.presentation.components.ReturnScaffold
import com.ertools.demooder.presentation.viewmodel.SettingsViewModel
import com.ertools.demooder.utils.Validation

@Composable
fun SettingsView(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val data = remember { mutableStateOf(emptyList<OptionData>()) }
    val deviceDamping = settingsViewModel.deviceDamping.collectAsState()
    val signalDetectionPeriod = settingsViewModel.signalDetectionPeriod.collectAsState()
    val enableNotifications = settingsViewModel.enableNotifications.collectAsState()
    val angerDetectionTime = settingsViewModel.angerDetectionTime.collectAsState()

    LaunchedEffect(true) {
        data.value = listOf(
            OptionData.InputOptionData(
                optionTitle = "Device damping",
                value = deviceDamping,
                onValidate = { Validation.isNumberInRange(it, -100, 100) },
                onSave = { settingsViewModel.saveDeviceDamping(it) }
            ),
            OptionData.InputOptionData(
                optionTitle = "Signal detection period in seconds",
                value = signalDetectionPeriod,
                onValidate = { Validation.isNumberInRange(it, 1, 10) },
                onSave = { settingsViewModel.saveSignalDetectionPeriod(it) }
            ),
            OptionData.SwitchOptionData(
                optionTitle = "Enable notifications",
                value = enableNotifications,
                onSave = { settingsViewModel.saveEnableNotifications(it) }
            ),
            OptionData.InputOptionData(
                optionTitle = "Anger detection time in seconds before trigger notification",
                value = angerDetectionTime,
                onValidate = { Validation.isNumberInRange(it, 1, 60) },
                onSave = { settingsViewModel.saveAngerDetectionTime(it) }
            )
        )
    }
    ReturnScaffold(
        viewName = stringResource(R.string.nav_settings),
        onReturn = { navController.popBackStack() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            items(data.value) {
                it.SettingsOption()
            }
        }
    }
}

@Preview
@Composable
fun SettingsViewPreview() {
    SettingsView(navController = rememberNavController())
}
