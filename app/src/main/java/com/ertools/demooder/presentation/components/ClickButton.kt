package com.ertools.demooder.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

/**
 * Displays a button with an icon.
 * @param onClick Action to be performed when the button is clicked.
 * @param iconResource Resource ID of the icon to be displayed.
 * @param iconContentDescriptionResource Resource ID of the content description for the icon.
 * @param modifier Modifier to be applied to the button.
 */
@Composable
fun ClickButton(
    modifier: Modifier = Modifier,
    iconResource: Int,
    iconContentDescriptionResource: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Icon(
            painter = painterResource(iconResource),
            modifier = Modifier.fillMaxSize(),
            contentDescription = stringResource(iconContentDescriptionResource)
        )
    }
}