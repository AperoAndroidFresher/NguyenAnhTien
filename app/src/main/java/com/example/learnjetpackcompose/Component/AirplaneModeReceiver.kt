package com.example.learnjetpackcompose.Component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log

class AirplaneModeReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        if (Intent.ACTION_AIRPLANE_MODE_CHANGED == intent?.action) {

            val isAirplaneModeOn = Settings.Global.getInt(
                context?.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON, 0
            ) != 0

            if (isAirplaneModeOn) {
                Log.d("Airplane", "Chế độ máy bay ĐÃ BẬT")
            } else {
                Log.d("Airplane", "Chế độ máy bay ĐÃ TẮT")
            }
        }
    }
}