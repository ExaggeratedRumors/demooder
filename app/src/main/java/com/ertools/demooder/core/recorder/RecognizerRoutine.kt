package com.ertools.demooder.core.recorder

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.core.app.ActivityCompat.startActivityForResult
import com.ertools.demooder.core.host.MainActivity

class RecognizerRoutine (private val activity: MainActivity){
    val intent = Intent(
        RecognizerIntent.ACTION_RECOGNIZE_SPEECH
    ).putExtra(
        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
    ).putExtra(
        RecognizerIntent.EXTRA_PROMPT,
        "Start Speaking"
    )

    fun runRecognizer() {
        startActivityForResult(activity, intent, 100, null)

    }

}