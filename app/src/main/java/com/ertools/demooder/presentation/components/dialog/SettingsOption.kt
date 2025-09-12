package com.ertools.demooder.presentation.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ertools.demooder.R

sealed class OptionData(
    val optionTitle: String,
) {
    @Composable
    abstract fun SettingsOption(modifier: Modifier)

    class InputOptionData(
        optionTitle: String,
        val value: State<String>,
        val unit: String? = null,
        val onValidate: @Composable (String) -> Boolean,
        private val onSave: (String) -> Unit
    ) : OptionData(optionTitle) {

        @Composable
        override fun SettingsOption(modifier: Modifier) {
            val option = this
            Row(
                modifier = modifier
            ) {
                val inputText = remember { mutableStateOf("") }
                val showDialog = remember { mutableStateOf(false) }

                Button(
                    onClick = {
                        showDialog.value = true
                    },
                    modifier = Modifier.background(color = Color.Transparent),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.9f),
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = option.optionTitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${value.value}$unit",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                if(showDialog.value) {
                    TextInputDialog(
                        title = option.optionTitle,
                        onDismiss = {
                            showDialog.value = false
                        },
                        onConfirm = {
                            showDialog.value = false
                            //if(option.onValidate(it)) {
                            inputText.value = it
                            option.onSave(it)
                            //}
                            inputText.value = it
                        }
                    )
                }
            }
        }
    }

    class SwitchOptionData(
        optionTitle: String,
        private val value: State<Boolean>,
        private val onSave: (Boolean) -> Unit
    ) : OptionData(optionTitle) {
        @Composable
        override fun SettingsOption(modifier: Modifier) {
            val option = this
            Row(
                modifier = modifier
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = dimensionResource(R.dimen.global_padding))
                    ,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option.optionTitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Switch(
                            checked = option.value.value,
                            onCheckedChange = {
                                option.onSave(it)
                            },
                            colors = SwitchColors(
                                checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                                checkedTrackColor = MaterialTheme.colorScheme.background,
                                checkedBorderColor = MaterialTheme.colorScheme.tertiary,
                                checkedIconColor = MaterialTheme.colorScheme.background,
                                uncheckedIconColor = MaterialTheme.colorScheme.background,
                                uncheckedBorderColor = MaterialTheme.colorScheme.secondary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                                uncheckedTrackColor = MaterialTheme.colorScheme.background,
                                disabledCheckedIconColor = MaterialTheme.colorScheme.onSurface,
                                disabledUncheckedIconColor = MaterialTheme.colorScheme.onSurface,
                                disabledCheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                disabledUncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                disabledUncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                                disabledCheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                                disabledCheckedBorderColor = MaterialTheme.colorScheme.onSurface,
                                disabledUncheckedBorderColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
        }
    }
}