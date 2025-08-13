package com.ertools.demooder.presentation.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ertools.demooder.R
import com.ertools.demooder.core.audio.RecordingFile
import com.ertools.demooder.presentation.navigation.RecordsNavigationItem
import com.ertools.demooder.presentation.viewmodel.FilesViewModel
import com.ertools.demooder.presentation.viewmodel.TabViewModel
import com.ertools.demooder.utils.AppConstants

@Composable
fun RecordsView(
    navController: NavController
) {
    val context = LocalContext.current.applicationContext
    val tabViewModel = viewModel<TabViewModel>()
    val filesViewModel = viewModel<FilesViewModel>().apply {
        this.loadExternalRecordings(context)
    }

    Column(modifier = Modifier.fillMaxSize().draggable(
        state = tabViewModel.dragState.value!!,
        orientation = Orientation.Horizontal,
        onDragStarted = {  },
        onDragStopped = { tabViewModel.updateTabIndexBasedOnSwipe() }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RecordsColumn(navController, context, filesViewModel.externalFiles)
    }
}

@Composable
fun ColumnScope.RecordsColumn(
    navController: NavController,
    context: Context,
    files: State<List<RecordingFile>>
) {
    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            items(files.value.size) {
                RecordItemView(
                    file = files.value[it],
                    iconResource = R.drawable.music_outlined,
                    contentDescriptionResource = R.string.records_icon_cd,
                    onClick = { file ->
                        if(!file.isWav) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.error_not_wav),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@RecordItemView
                        }
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = AppConstants.BUNDLE_KEY_RECORDS_FILE,
                            value = file
                        )
                        navController.navigate(route = RecordsNavigationItem.Player.route)
                    }
                )
            }
        }
    }
}

@Composable
fun RecordItemView(
    modifier: Modifier = Modifier,
    file: RecordingFile,
    iconResource: Int,
    contentDescriptionResource: Int,
    onClick: (RecordingFile) -> Unit
) {
    Button(
        onClick = { onClick(file) },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(iconResource),
                modifier = Modifier.padding(8.dp),
                contentDescription = stringResource(contentDescriptionResource)
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = file.name,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                )
                Text(
                    text = file.modificationDate,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontWeight = MaterialTheme.typography.titleSmall.fontWeight
                )
            }
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = file.size,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    fontWeight = MaterialTheme.typography.labelSmall.fontWeight
                )
            }
        }
    }
}
