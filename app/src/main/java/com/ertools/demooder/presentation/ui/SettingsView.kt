package com.ertools.demooder.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ertools.demooder.R
import com.ertools.demooder.core.settings.SettingsStore
import com.ertools.demooder.presentation.components.OptionData
import com.ertools.demooder.presentation.components.ReturnScaffold
import com.ertools.demooder.presentation.viewmodel.SettingsViewModel
import com.ertools.demooder.presentation.viewmodel.SettingsViewModelFactory
import com.ertools.demooder.utils.Validation

@Composable
fun SettingsView(
    navController: NavHostController
) {
    /** Settings objects **/
    val context = LocalContext.current.applicationContext
    val settingsStore = SettingsStore(context)
    val settingsViewModelFactory = remember { SettingsViewModelFactory(settingsStore) }
    val settingsViewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)

    /** Values **/
    val data = remember { mutableStateOf(emptyList<OptionData>()) }
    val deviceDamping = settingsViewModel.deviceDamping.collectAsState()
    val signalDetectionPeriod = settingsViewModel.signalDetectionPeriod.collectAsState()
    val enableNotifications = settingsViewModel.enableNotifications.collectAsState()
    val angerDetectionTime = settingsViewModel.angerDetectionTime.collectAsState()

    /** Strings **/
    val deviceDampingTitle = stringResource(R.string.settings_device_damping_title)
    val deviceDampingUnit = stringResource(R.string.settings_device_damping_unit)
    val signalDetectionPeriodTitle = stringResource(R.string.settings_signal_detection_period_title)
    val signalDetectionPeriodUnit = stringResource(R.string.settings_signal_detection_period_unit)
    val enableNotificationsTitle = stringResource(R.string.settings_enable_notifications_title)
    val angerDetectionTimeTitle = stringResource(R.string.settings_anger_detection_time_title)
    val angerDetectionTimeUnit = stringResource(R.string.settings_anger_detection_time_unit)


    LaunchedEffect(true) {
        data.value = listOf(
            OptionData.InputOptionData(
                optionTitle = deviceDampingTitle,
                value = deviceDamping,
                unit = deviceDampingUnit,
                onValidate = { Validation.isNumberInRange(it, -100, 100) },
                onSave = { settingsViewModel.saveDeviceDamping(it) }
            ),
            OptionData.InputOptionData(
                optionTitle = signalDetectionPeriodTitle,
                value = signalDetectionPeriod,
                unit = signalDetectionPeriodUnit,
                onValidate = { Validation.isNumberInRange(it, 1, 10) },
                onSave = { settingsViewModel.saveSignalDetectionPeriod(it) }
            ),
            OptionData.SwitchOptionData(
                optionTitle = enableNotificationsTitle,
                value = enableNotifications,
                onSave = { settingsViewModel.saveEnableNotifications(it) }
            ),
            OptionData.InputOptionData(
                optionTitle = angerDetectionTimeTitle,
                value = angerDetectionTime,
                unit = angerDetectionTimeUnit,
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
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = dimensionResource(R.dimen.global_divider_thickness),
                    color = MaterialTheme.colorScheme.onBackground
                )
                it.SettingsOption(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f)
                        .padding(dimensionResource(R.dimen.global_padding)))
            }
        }
    }
}

@Preview
@Composable
fun SettingsViewPreview() {
    SettingsView(navController = rememberNavController())
}
