package com.ertools.demooder.presentation.components.dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

/**
 * Displays a button that toggles between two states with different icons.
 * @param modifier Modifier to be applied to the button.
 * @param turnState State representing the current toggle state (enabled/disabled).
 * @param trueStateIconResource Resource ID of the icon when the state is enabled.
 * @param falseIconResource Resource ID of the icon when the state is disabled.
 * @param iconContentDescriptionResource Resource ID of the content description for the icon.
 * @param onClick Action to be performed when the button is clicked.
 */
@Composable
fun StateButton(
    modifier: Modifier = Modifier,
    turnState: State<Boolean>,
    trueStateIconResource: Int,
    falseIconResource: Int,
    iconContentDescriptionResource: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (turnState.value) MaterialTheme.colorScheme.tertiary
            else MaterialTheme.colorScheme.primary
        ),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Icon(
            painter = if(turnState.value) painterResource(falseIconResource)
            else painterResource(trueStateIconResource),
            modifier = Modifier.fillMaxSize(),
            contentDescription = stringResource(iconContentDescriptionResource)
        )
    }
}