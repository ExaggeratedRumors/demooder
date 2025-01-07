package com.ertools.demooder.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import com.ertools.demooder.utils.SETTINGS_DEFAULT_ANGER_DETECTION_TIME
import com.ertools.demooder.utils.SETTINGS_DEFAULT_DEVICE_DAMPING
import com.ertools.demooder.utils.SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS
import com.ertools.demooder.utils.SETTINGS_DEFAULT_SIGNAL_DETECTION_PERIOD
import com.ertools.demooder.utils.Validation


@Preview
@Composable
fun SettingsViewPreview() {
    SettingsView(navController = rememberNavController())
}

@Composable
fun SettingsView(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val data = listOf(
        OptionData.InputOptionData(
            optionTitle = "Device damping",
            defaultValue = SETTINGS_DEFAULT_DEVICE_DAMPING,
            onValidate = { Validation.isNumberInRange(it, -100, 100) },
            onSave = {
                settingsViewModel.saveDeviceDamping(it.currentValue.value)
            }
        ),
        OptionData.InputOptionData(
            optionTitle = "Signal detection period",
            defaultValue = SETTINGS_DEFAULT_SIGNAL_DETECTION_PERIOD,
            onValidate = { Validation.isNumberInRange(it, 1, 10) },
            onSave = { settingsViewModel.saveSignalDetectionPeriod(it.currentValue.value) }
        ),
        OptionData.SwitchOptionData(
            optionTitle = "Enable notifications",
            defaultValue = SETTINGS_DEFAULT_ENABLE_NOTIFICATIONS,
            onSave = { settingsViewModel.saveEnableNotifications(it.currentValue.value) }
        ),
        OptionData.InputOptionData(
            optionTitle = "Anger detection time before trigger notification",
            defaultValue = SETTINGS_DEFAULT_ANGER_DETECTION_TIME,
            onValidate = { Validation.isNumberInRange(it, 1, 60) },
            onSave = { settingsViewModel.saveAngerDetectionTime(it.currentValue.value) }
        )
    )

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
            items(data) {
                it.SettingsOption()
            }
        }
    }
}


