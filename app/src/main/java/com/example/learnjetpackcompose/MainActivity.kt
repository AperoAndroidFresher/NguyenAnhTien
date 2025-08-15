package com.example.learnjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.example.learnjetpackcompose.ui.theme.LearnJetPackComposeTheme
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.Context
import dagger.hilt.android.AndroidEntryPoint
import com.example.learnjetpackcompose.Utils.requestStoragePermission
import com.example.learnjetpackcompose.Utils.LanguagePreferences
import com.example.learnjetpackcompose.Utils.updateLocale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var onPermissionGrantedCallback: (() -> Unit)? = null

    fun requestPermission(onPermissionGranted: (() -> Unit)? = null) {
        onPermissionGrantedCallback = onPermissionGranted
        requestStoragePermission(this)
    }

    override fun attachBaseContext(newBase: Context) {
        val langPref = LanguagePreferences(newBase).getLanguage()
        val updated = updateLocale(newBase, langPref)
        super.attachBaseContext(updated)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LearnJetPackComposeTheme {
                Surface(){
                    NavigationApp()
                }
            }
        }
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGrantedCallback?.invoke()
                onPermissionGrantedCallback = null
            } else {
            }
        }
    }
}










