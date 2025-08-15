package com.example.learnjetpackcompose.data.playback

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.data.model.PlaybackManager
import com.example.learnjetpackcompose.data.service.MusicService
import com.example.learnjetpackcompose.domain.playback.PlaybackGateway
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackGatewayImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : PlaybackGateway {

    override val currentSong = PlaybackManager.currentSong
    override val isPlaying = PlaybackManager.isPlaying
    override val isShuffle = PlaybackManager.isShuffle
    override val repeatMode = PlaybackManager.repeatMode
    override val currentPosition = PlaybackManager.currentPosition
    override val duration = PlaybackManager.duration

    override fun setQueue(
        songs: List<Song>,
        startIndex: Int,
        source: PlaybackManager.QueueSource,
        id: String?
    ) {
        PlaybackManager.setQueue(songs, startIndex, source, id)
    }

    override fun play(song: Song) {
        val intent = Intent(appContext, MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putExtra(MusicService.EXTRA_SONG_ID, song.songId)
            putExtra(MusicService.EXTRA_SONG_TITLE, song.title)
            putExtra(MusicService.EXTRA_SONG_ARTIST, song.artist)
            putExtra(MusicService.EXTRA_SONG_DATA, song.data)
            putExtra(MusicService.EXTRA_SONG_DURATION, song.duration)
            putExtra(MusicService.EXTRA_SONG_ALBUM_ART, song.albumArt)
        }
        ContextCompat.startForegroundService(appContext, intent)
    }

    override fun togglePlayPause() {
        val song = PlaybackManager.currentSong.value ?: return
        val action = if (PlaybackManager.isPlaying.value) MusicService.ACTION_PAUSE else MusicService.ACTION_PLAY
        val intent = Intent(appContext, MusicService::class.java).apply {
            this.action = action
            putExtra(MusicService.EXTRA_SONG_TITLE, song.title)
            putExtra(MusicService.EXTRA_SONG_ARTIST, song.artist)
            putExtra(MusicService.EXTRA_SONG_DATA, song.data)
            putExtra(MusicService.EXTRA_SONG_DURATION, song.duration)
            putExtra(MusicService.EXTRA_SONG_ALBUM_ART, song.albumArt)
        }
        ContextCompat.startForegroundService(appContext, intent)
    }

    override fun stop() {
        val intent = Intent(appContext, MusicService::class.java).apply { action = MusicService.ACTION_STOP }
        appContext.startService(intent)
        PlaybackManager.setNowPlaying(null)
        PlaybackManager.setIsPlaying(false)
    }

    override fun next() {
        val intent = Intent(appContext, MusicService::class.java).apply { action = MusicService.ACTION_NEXT }
        ContextCompat.startForegroundService(appContext, intent)
    }

    override fun previous() {
        val intent = Intent(appContext, MusicService::class.java).apply { action = MusicService.ACTION_PREVIOUS }
        ContextCompat.startForegroundService(appContext, intent)
    }

    override fun seekTo(position: Long) {
        val intent = Intent(appContext, MusicService::class.java).apply {
            action = MusicService.ACTION_SEEK
            putExtra(MusicService.EXTRA_SEEK_POSITION, position)
        }
        appContext.startService(intent)
    }

    override fun toggleShuffle() {
        PlaybackManager.toggleShuffle()
    }

    override fun cycleRepeatMode() {
        PlaybackManager.cycleRepeatMode()
    }
}