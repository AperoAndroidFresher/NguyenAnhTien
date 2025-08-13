package com.example.learnjetpackcompose.data.repository

import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.data.model.PlaybackManager
import com.example.learnjetpackcompose.Screen.Player.RepeatMode
import com.example.learnjetpackcompose.domain.playback.PlaybackGateway
import com.example.learnjetpackcompose.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepositoryImpl @Inject constructor(
    private val playbackGateway: PlaybackGateway
) : PlayerRepository {

    override val currentSong = playbackGateway.currentSong
    override val isPlaying: StateFlow<Boolean> = playbackGateway.isPlaying
    override val isShuffle: StateFlow<Boolean> = playbackGateway.isShuffle
    override val repeatMode: StateFlow<RepeatMode> = playbackGateway.repeatMode
    override val currentPosition: StateFlow<Long> = playbackGateway.currentPosition
    override val duration: StateFlow<Long> = playbackGateway.duration

    override fun playSong(song: Song) = playbackGateway.play(song)

    override fun togglePlayPause() = playbackGateway.togglePlayPause()

    override fun stopPlayback() = playbackGateway.stop()

    override fun skipToNext() = playbackGateway.next()

    override fun skipToPrevious() = playbackGateway.previous()

    override fun seekTo(position: Long) = playbackGateway.seekTo(position)

    override fun setQueueFromLocal(songs: List<Song>, startIndex: Int) {
        playbackGateway.setQueue(songs, startIndex, PlaybackManager.QueueSource.LOCAL, id = null)
    }

    override fun setQueueFromRemote(songs: List<Song>, startIndex: Int, queryId: String?) {
        playbackGateway.setQueue(songs, startIndex, PlaybackManager.QueueSource.REMOTE, id = queryId)
    }

    override fun setQueueFromPlaylist(playlistId: String, songs: List<Song>, startIndex: Int) {
        playbackGateway.setQueue(songs, startIndex, PlaybackManager.QueueSource.PLAYLIST, id = playlistId)
    }

    override fun toggleShuffle() = playbackGateway.toggleShuffle()

    override fun cycleRepeatMode() = playbackGateway.cycleRepeatMode()
}