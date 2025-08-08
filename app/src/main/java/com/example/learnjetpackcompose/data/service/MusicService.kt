package com.example.learnjetpackcompose.data.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MusicService : Service() {

    companion object{
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    private val binder = MusicBinder()
//    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(){
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_PLAY -> {

            }

            ACTION_PAUSE -> {

            }

            ACTION_STOP -> {

            }

            ACTION_NEXT -> {

            }
            ACTION_PREVIOUS -> {

            }
        }
        return START_NOT_STICKY
        }


}