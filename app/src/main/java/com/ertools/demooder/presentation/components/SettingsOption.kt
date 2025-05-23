package com.ertools.demooder.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.unit.dp
import java.util.Locale

sealed class OptionData(
    val optionTitle: String,
) {
    abstract fun valueToString(): String
    @Composable
    abstract fun SettingsOption()

    class InputOptionData(
        optionTitle: String,
        val value: State<Double>,
        val onValidate: @Composable (String) -> Boolean,
        private val onSave: (Double) -> Unit
    ) : OptionData(optionTitle) {
        override fun valueToString(): String = "%.1f".format(Locale.ENGLISH, value.value)

        @Composable
        override fun SettingsOption() {
            val option = this
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .padding(20.dp)
            ) {
                val inputText = remember { mutableStateOf("") }
                val showDialog = remember { mutableStateOf(false) }

                Button(
                    onClick = {
                        showDialog.value = true
                    },
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Bottom)
                        .background(color = Color.Transparent),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .fillMaxHeight(),
                    ) {
                        Text(
                            text = option.optionTitle,
                            fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = valueToString(),
                            fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                            color = MaterialTheme.colorScheme.onBackground
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
                                option.onSave(it.toDouble())
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
        override fun valueToString(): String = "${value.value}"

        @Composable
        override fun SettingsOption() {
            val option = this
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .padding(20.dp)
            ) {
                /** Switch **/
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 20.dp)
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
                            fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                            color = MaterialTheme.colorScheme.onSurface
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