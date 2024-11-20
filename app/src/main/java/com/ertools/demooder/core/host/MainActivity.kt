package com.ertools.demooder.core.host

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ertools.demooder.presentation.ui.MainView
import com.ertools.demooder.presentation.viewmodel.MainViewModel
import com.ertools.demooder.utils.PERMISSIONS.REQUIRED_PERMISSIONS

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        val permissionsGained = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                this, it
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
        if(!permissionsGained)
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView(viewModel)
        }
    }
}