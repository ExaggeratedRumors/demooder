package com.ertools.demooder.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ertools.demooder.R


@Preview
@Composable
fun SettingsViewPreview() {
    SettingsView(navController = rememberNavController())
}

@Composable
fun SettingsView(
    navController: NavHostController
) {
    val data = listOf(
        OptionData(
            optionTitle = "A",
            currentValue = "b",
        ) { navController.navigate("a") },
        OptionData(
            optionTitle = "B",
            currentValue = "b"
        ) { navController.navigate("b") },
        OptionData(
            optionTitle = "C",
            currentValue = "c"
        ) { navController.navigate("c") }
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        items(data) {
            SettingsOption(it)
        }
    }
}

data class OptionData(
    val optionTitle: String,
    val currentValue: String,
    val onClick: () -> Unit
)

@Composable
fun SettingsOption(
    data: OptionData
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(),
        ){
            Text(
                text = data.optionTitle,
                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = data.currentValue,
                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Button(
            onClick = { data.onClick() },
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Bottom)
                .background(color = Color.Transparent),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.settings_link),
                contentDescription = "Settings",
                modifier = Modifier
                    .rotate(90f)
                    .scale(2f)
                    .align(Alignment.Bottom)
            )
        }
    }
}