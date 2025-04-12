package com.ertools.demooder.presentation.viewmodel

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.provider.MediaStore
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.Date
import java.util.Locale

class FilesViewModel : ViewModel() {
    data class RecordingFile(
        val name: String,
        val modificationDate: String,
        val size: String
    )

    private var _externalFiles = mutableStateOf<List<RecordingFile>>(emptyList())
    val externalFiles: State<List<RecordingFile>> = _externalFiles

    private var _internalFiles = mutableStateOf<List<RecordingFile>>(emptyList())
    val internalFiles: State<List<RecordingFile>> = _internalFiles

    fun loadInternalRecordings(context: Context) {
        val audioFiles = mutableListOf<RecordingFile>()
        val contentUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_INTERNAL)
        val columns = arrayOf(
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media.SIZE
        )
        val query = "${MediaStore.Audio.Media.IS_RECORDING} != 0"
        val order = "${MediaStore.Audio.Media.DATE_ADDED} DESC"
        context.contentResolver.query(
            contentUri, columns, query, null, order
        )?.use { cursor ->
            val filenameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val modificationDateColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            while (cursor.moveToNext()) audioFiles.add(
                RecordingFile(
                    cursor.getString(filenameColumn),
                    dateFormat(cursor.getString(modificationDateColumn)),
                    bytesFormat(cursor.getLong(sizeColumn))
                )
            )
        }
        _internalFiles.value = audioFiles
    }

    fun loadExternalRecordings(context: Context) {
        val audioFiles = mutableListOf<RecordingFile>()
        val contentUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val columns = arrayOf(
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media.SIZE
        )
        val query = "${MediaStore.Audio.Media.IS_RECORDING} != 0"
        val order = "${MediaStore.Audio.Media.DATE_ADDED} DESC"
        context.contentResolver.query(
            contentUri, columns, query, null, order
        )?.use { cursor ->
            val filenameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val modificationDateColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            while (cursor.moveToNext()) audioFiles.add(
                RecordingFile(
                    cursor.getString(filenameColumn),
                    dateFormat(cursor.getString(modificationDateColumn)),
                    bytesFormat(cursor.getLong(sizeColumn))
                )
            )
        }
        _externalFiles.value = audioFiles
    }


    private fun dateFormat(date: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        return sdf.format(Date(date.toLong() * 1000)).toString()
    }

    private fun bytesFormat(size: Long): String {
        return when(size) {
            in 0..1024 -> "$size B"
            in 1025..1048576 -> "${"%.2f".format(size / 1024.0, Locale.ENGLISH)} KB"
            in 1048577..1073741824 -> "${"%.2f".format(size / 1048576.0, Locale.ENGLISH)} MB"
            else -> "${"%.2f".format(size / 1073741824.0, Locale.ENGLISH)} GB"
        }
    }
}

class FilesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilesViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}