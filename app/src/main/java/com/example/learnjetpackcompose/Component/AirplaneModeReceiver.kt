package com.example.learnjetpackcompose.Component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log // Import lớp Log

class AirplaneModeReceiver : BroadcastReceiver() {

    // Nên định nghĩa một TAG để dễ dàng tìm kiếm Log trong Logcat
    private val TAG = "AirplaneModeReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        // Kiểm tra action của Intent
        if (Intent.ACTION_AIRPLANE_MODE_CHANGED == intent?.action) {
            // Lấy trạng thái chế độ máy bay
            val isAirplaneModeOn = Settings.Global.getInt(
                context?.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON, 0
            ) != 0

            // Ghi thông báo vào Logcat thay vì hiển thị Toast
            if (isAirplaneModeOn) {
                Log.d(TAG, "Chế độ máy bay ĐÃ BẬT") // Log Debug
            } else {
                Log.d(TAG, "Chế độ máy bay ĐÃ TẮT") // Log Debug
            }
        }
    }
}