package com.example.learnjetpackcompose.data.repository

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.data.model.PlaybackManager
import com.example.learnjetpackcompose.data.service.MusicService
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for handling music playback operations
 */
@Singleton
class PlayerRepository @Inject constructor() {

    val currentSong = PlaybackManager.currentSong
    val isPlaying: StateFlow<Boolean> = PlaybackManager.isPlaying

    fun playSong(context: Context, song: Song) {
        val intent = Intent(context, MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putExtra(MusicService.EXTRA_SONG_TITLE, song.title)
            putExtra(MusicService.EXTRA_SONG_ARTIST, song.artist)
            putExtra(MusicService.EXTRA_SONG_DATA, song.data)
            putExtra(MusicService.EXTRA_SONG_DURATION, song.duration)
            putExtra(MusicService.EXTRA_SONG_ALBUM_ART, song.albumArt)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun togglePlayPause(context: Context) {
        val song = currentSong.value ?: return
        val action = if (isPlaying.value) MusicService.ACTION_PAUSE else MusicService.ACTION_PLAY

        val intent = Intent(context, MusicService::class.java).apply {
            this.action = action
            putExtra(MusicService.EXTRA_SONG_TITLE, song.title)
            putExtra(MusicService.EXTRA_SONG_ARTIST, song.artist)
            putExtra(MusicService.EXTRA_SONG_DATA, song.data)
            putExtra(MusicService.EXTRA_SONG_DURATION, song.duration)
            putExtra(MusicService.EXTRA_SONG_ALBUM_ART, song.albumArt)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun stopPlayback(context: Context) {
        val intent = Intent(context, MusicService::class.java).apply {
            action = MusicService.ACTION_STOP
        }
        ContextCompat.startForegroundService(context, intent)
        PlaybackManager.setNowPlaying(null)
        PlaybackManager.setIsPlaying(false)
    }

    fun skipToNext(context: Context) {
        val intent = Intent(context, MusicService::class.java).apply {
            action = MusicService.ACTION_NEXT
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun skipToPrevious(context: Context) {
        val intent = Intent(context, MusicService::class.java).apply {
            action = MusicService.ACTION_PREVIOUS
        }
        ContextCompat.startForegroundService(context, intent)
    }
}
