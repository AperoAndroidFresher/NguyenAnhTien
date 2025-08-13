package com.example.learnjetpackcompose.data.service

import android.annotation.SuppressLint
import android.app.Service
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.example.learnjetpackcompose.MainActivity
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.data.model.PlaybackManager
import android.util.Log

data class PlaybackState(
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0,
    val duration: Long = 0,
    val currentSong: Song? = null
)

class MusicService : Service() {

    companion object{
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
        const val ACTION_SEEK = "ACTION_SEEK"

        const val EXTRA_SONG_ID = "EXTRA_SONG_ID"
        const val EXTRA_SONG_TITLE = "EXTRA_SONG_TITLE"
        const val EXTRA_SONG_ARTIST = "EXTRA_SONG_ARTIST"
        const val EXTRA_SONG_DATA = "EXTRA_SONG_DATA"
        const val EXTRA_SONG_DURATION = "EXTRA_SONG_DURATION"
        const val EXTRA_SONG_ALBUM_ART = "EXTRA_SONG_ALBUM_ART"
        const val EXTRA_SEEK_POSITION = "EXTRA_SEEK_POSITION"

        private const val NOTIFICATION_CHANNEL_ID = "music_playback_channel"
        private const val NOTIFICATION_CHANNEL_NAME = "Music Playback"
        private const val NOTIFICATION_ID = 1001

        private const val POSITION_UPDATE_INTERVAL = 1000L // 1 second
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private var mediaPlayer: MediaPlayer? = null
    private var currentTitle: String = ""
    private var currentArtist: String = ""
    private var currentData: String = ""
    private var currentAlbumArt: String? = null

    // Position tracking
    private val positionHandler = Handler(Looper.getMainLooper())
    private var positionRunnable: Runnable? = null

    override fun onCreate(){
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_PLAY -> {
                val id = intent.getLongExtra(EXTRA_SONG_ID, 0L)
                val title = intent.getStringExtra(EXTRA_SONG_TITLE) ?: ""
                val artist = intent.getStringExtra(EXTRA_SONG_ARTIST) ?: ""
                val data = intent.getStringExtra(EXTRA_SONG_DATA) ?: ""
                val duration = intent.getStringExtra(EXTRA_SONG_DURATION) ?: "0:00"
                val albumArt = intent.getStringExtra(EXTRA_SONG_ALBUM_ART) ?: ""
                if (mediaPlayer != null && currentData == data && mediaPlayer?.isPlaying == false) {
                    resumePlayback()
                } else {
                    startPlayback(id, title, artist, data, duration, albumArt)
                }
            }

            ACTION_PAUSE -> {
                pausePlayback()
            }

            ACTION_STOP -> {
                stopPlayback()
            }

            ACTION_SEEK -> {
                val position = intent.getLongExtra(EXTRA_SEEK_POSITION, 0L)
                seekToPosition(position)
            }

            ACTION_NEXT -> {
                val next = PlaybackManager.nextManual()
                if (next != null) {
                    startPlayback(next.songId, next.title, next.artist, next.data, next.duration, next.albumArt)
                } else {
                    updateNotification(isPlaying = false)
                    PlaybackManager.setIsPlaying(false)
                    stopSelf()
                }
            }

            ACTION_PREVIOUS -> {
                val prev = PlaybackManager.previousManual()
                if (prev != null) {
                    startPlayback(prev.songId, prev.title, prev.artist, prev.data, prev.duration, prev.albumArt)
                } else {
                    updateNotification(isPlaying = false)
                    PlaybackManager.setIsPlaying(false)
                    stopSelf()
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun startPlayback(id: Long, title: String, artist: String, data: String, duration: String, albumArt: String?) {
        currentTitle = title
        currentArtist = artist
        currentData = data
        currentAlbumArt = albumArt

        PlaybackManager.setNowPlaying(Song(id, title, artist, albumArt, duration, data))

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

            try {
                if (data.startsWith("content://") || data.startsWith("file://") || data.startsWith("http")) {
                    setDataSource(applicationContext, Uri.parse(data))
                } else {
                    setDataSource(data)
                }
            } catch (e: Exception) {
                startForeground(NOTIFICATION_ID, buildNotification(title, artist, isPlaying = false))
                stopSelf()
                return
            }

            setOnPreparedListener {
                it.start()
                updateNotification(isPlaying = true)
                PlaybackManager.setIsPlaying(true)

                // Update duration and start position tracking
                val totalDuration = it.duration.toLong()
                PlaybackManager.setDuration(totalDuration)
                startPositionUpdates()
            }

            setOnCompletionListener {
                stopPositionUpdates()
                val next = PlaybackManager.onSongCompleted()
                if (next != null) {
                    startPlayback(next.songId, next.title, next.artist, next.data, next.duration, next.albumArt)
                } else {
                    updateNotification(isPlaying = false)
                    PlaybackManager.setIsPlaying(false)
                    stopSelf()
                }
            }

            setOnErrorListener { _, _, _ ->
                stopPositionUpdates()
                updateNotification(isPlaying = false)
                PlaybackManager.setIsPlaying(false)
                stopSelf()
                true
            }

            prepareAsync()
        }

        val notification = buildNotification(title, artist, isPlaying = false)
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun pausePlayback() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                updateNotification(isPlaying = false)
                PlaybackManager.setIsPlaying(false)
                stopPositionUpdates()
            }
        }
    }

    private fun stopPlayback() {
        stopPositionUpdates()
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        PlaybackManager.setIsPlaying(false)
        PlaybackManager.setCurrentPosition(0)
        PlaybackManager.setDuration(0)
    }

    private fun resumePlayback() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                updateNotification(isPlaying = true)
                PlaybackManager.setIsPlaying(true)
                startPositionUpdates()
            }
        }
    }

    private fun seekToPosition(position: Long) {
        mediaPlayer?.let {
            try {
                it.seekTo(position.toInt())
                PlaybackManager.setCurrentPosition(position)
            } catch (e: Exception) {
                Log.e("MusicService", "Error seeking to position: ${e.message}")
            }
        }
    }

    private fun startPositionUpdates() {
        stopPositionUpdates() // Stop any existing updates
        positionRunnable = object : Runnable {
            override fun run() {
                mediaPlayer?.let { player ->
                    if (player.isPlaying) {
                        val currentPos = player.currentPosition.toLong()
                        PlaybackManager.setCurrentPosition(currentPos)
                    }
                }
                positionHandler.postDelayed(this, POSITION_UPDATE_INTERVAL)
            }
        }
        positionHandler.post(positionRunnable!!)
    }

    private fun stopPositionUpdates() {
        positionRunnable?.let {
            positionHandler.removeCallbacks(it)
            positionRunnable = null
        }
    }

    private fun buildNotification(title: String, artist: String, isPlaying: Boolean): Notification {
        val openAppPendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(Intent(this@MusicService, MainActivity::class.java))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(0, android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE)
            } else {
                getPendingIntent(0, android.app.PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }

        val pauseOrPlayActionIntent = Intent(this, MusicService::class.java).apply {
            action = if (isPlaying) ACTION_PAUSE else ACTION_PLAY
            putExtra(EXTRA_SONG_TITLE, currentTitle)
            putExtra(EXTRA_SONG_ARTIST, currentArtist)
            putExtra(EXTRA_SONG_DATA, currentData)
            putExtra(EXTRA_SONG_DURATION, "0:00")
        }
        val pauseOrPlayPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.app.PendingIntent.getService(this, 1, pauseOrPlayActionIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE)
        } else {
            android.app.PendingIntent.getService(this, 1, pauseOrPlayActionIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val previousIntent = Intent(this, MusicService::class.java).apply { action = ACTION_PREVIOUS }
        val previousPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.app.PendingIntent.getService(this, 2, previousIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE)
        } else {
            android.app.PendingIntent.getService(this, 2, previousIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val nextIntent = Intent(this, MusicService::class.java).apply { action = ACTION_NEXT }
        val nextPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.app.PendingIntent.getService(this, 3, nextIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE)
        } else {
            android.app.PendingIntent.getService(this, 3, nextIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val stopIntent = Intent(this, MusicService::class.java).apply { action = ACTION_STOP }
        val stopPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.app.PendingIntent.getService(this, 4, stopIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE)
        } else {
            android.app.PendingIntent.getService(this, 4, stopIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_music)
            .setContentTitle(title.ifEmpty { getString(R.string.app_name) })
            .setContentText(artist.ifEmpty { "Playing music" })
            .setContentIntent(openAppPendingIntent)
            .setOnlyAlertOnce(true)
            .setOngoing(isPlaying)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .addAction(
                android.R.drawable.ic_media_previous,
                "",
                previousPendingIntent
            )
            .addAction(
                if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
                "",
                pauseOrPlayPendingIntent
            )
            .addAction(
                android.R.drawable.ic_media_next,
                "",
                nextPendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "",
                stopPendingIntent
            )
            .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2, 3))
        return builder.build()
    }

    @SuppressLint("MissingPermission")
    private fun updateNotification(isPlaying: Boolean) {
        val notification = buildNotification(currentTitle, currentArtist, isPlaying)
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPositionUpdates()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}