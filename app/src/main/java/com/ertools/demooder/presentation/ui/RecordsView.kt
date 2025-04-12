package com.ertools.demooder.presentation.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ertools.demooder.R
import com.ertools.demooder.presentation.components.TabLayout
import com.ertools.demooder.presentation.viewmodel.FilesViewModel
import com.ertools.demooder.presentation.viewmodel.FilesViewModelFactory
import com.ertools.demooder.presentation.viewmodel.CardViewModel

@Composable
fun RecordsView() {
    val viewModel = viewModel<CardViewModel>()
    val context = LocalContext.current.applicationContext
    val filesViewModelFactory = remember {
        FilesViewModelFactory()
    }
    val filesViewModel: FilesViewModel = viewModel<FilesViewModel>(
        factory = filesViewModelFactory
    ).apply {
        this.loadExternalRecordings(context)
        this.loadInternalRecordings(context)
    }

    Column(modifier = Modifier.fillMaxSize().draggable(
        state = viewModel.dragState.value!!,
        orientation = Orientation.Horizontal,
        onDragStarted = {  },
        onDragStopped = {
            viewModel.updateTabIndexBasedOnSwipe()
        }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabLayout(
            views = listOf(
                stringResource(R.string.records_internal_storage) to { RecordsView(filesViewModel.internalFiles) },
                stringResource(R.string.records_external_storage) to { RecordsView(filesViewModel.externalFiles) }
            )
        )
    }
}

@Composable
fun ColumnScope.RecordsView(files: State<List<FilesViewModel.RecordingFile>>) {
    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            items(files.value.size) {
                RecordItemView(
                    name = files.value[it].name,
                    modificationDate = files.value[it].modificationDate,
                    size = files.value[it].size,
                    iconResource = R.drawable.outline_music_note_24,
                    contentDescriptionResource = R.string.records_icon_cd,
                    onClick = { /* Handle click */ }
                )
            }
        }
    }
}

@Composable
fun RecordItemView(
    modifier: Modifier = Modifier,
    name: String,
    modificationDate: String,
    size: String,
    iconResource: Int,
    contentDescriptionResource: Int,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
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
                    text = name,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                )
                Text(
                    text = modificationDate,
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
                    text = size.toString(),
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    fontWeight = MaterialTheme.typography.labelSmall.fontWeight
                )
            }
        }
    }
}

@Preview(name = "records", group = "records")
@Composable
fun RecordsViewPreview() {
    RecordsView()
}