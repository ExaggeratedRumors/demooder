package com.ertools.demooder.presentation.viewmodel

import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.ertools.demooder.core.audio.RecordingFile
import java.util.Date
import java.util.Locale

/**
 * ViewModel for loading External and Internal recordings from the device.
 */
class FilesViewModel(application: Application) : AndroidViewModel(application) {
    private var _externalFiles = mutableStateOf<List<RecordingFile>>(emptyList())
    val externalFiles: State<List<RecordingFile>> = _externalFiles

    /********************/
    /*******  API  ******/
    /********************/

    fun loadExternalRecordings(context: Context) {
        loadRecordings(context, MediaStore.VOLUME_EXTERNAL, _externalFiles)
        Log.d("FilesViewModel", "External files: ${_externalFiles.value}")
    }

    /*********************/
    /** Private methods **/
    /*********************/

    private fun loadRecordings(context: Context, source: String, dataStorage: MutableState<List<RecordingFile>>){
        val audioFiles = mutableListOf<RecordingFile>()
        val contentUri = MediaStore.Audio.Media.getContentUri(source)
        val columns = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media.SIZE
        )
        val query = "${MediaStore.Audio.Media.IS_RECORDING} != 0"
        val order = "${MediaStore.Audio.Media.DATE_ADDED} DESC"
        context.contentResolver.query(
            contentUri, columns, query, null, order
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val filenameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val modificationDateColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            while (cursor.moveToNext()) audioFiles.add(
                RecordingFile(
                    name = cursor.getString(filenameColumn),
                    modificationDate = dateFormat(cursor.getString(modificationDateColumn)),
                    size = bytesFormat(cursor.getLong(sizeColumn)),
                    uri = ContentUris.withAppendedId(contentUri, cursor.getLong(idColumn)),
                    isWav = cursor.getString(filenameColumn).endsWith(".wav", ignoreCase = true)
                )
            )
        }
        dataStorage.value = audioFiles
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