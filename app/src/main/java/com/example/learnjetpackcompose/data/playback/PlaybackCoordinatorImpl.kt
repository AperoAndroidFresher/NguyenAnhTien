package com.example.learnjetpackcompose.data.playback

import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.domain.playback.PlaybackCoordinator
import com.example.learnjetpackcompose.domain.playback.PreviewPlayback
import com.example.learnjetpackcompose.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackCoordinatorImpl @Inject constructor(
    private val previewPlayback: PreviewPlayback,
    private val playerRepository: PlayerRepository
) : PlaybackCoordinator {
    private val _mode = MutableStateFlow(PlaybackCoordinator.Mode.IDLE)
    override val mode: StateFlow<PlaybackCoordinator.Mode> = _mode.asStateFlow()

    private val _previewCurrentSong = MutableStateFlow<Song?>(null)
    private val _previewIsPlaying = MutableStateFlow(false)
    override val previewCurrentSong: StateFlow<Song?> = _previewCurrentSong.asStateFlow()
    override val previewIsPlaying: StateFlow<Boolean> = _previewIsPlaying.asStateFlow()

    override fun startPreview(song: Song) {
        try { playerRepository.stopPlayback() } catch (_: Exception) {}
        previewPlayback.play(song.data)
        _mode.value = PlaybackCoordinator.Mode.PREVIEW
        _previewCurrentSong.value = song
        _previewIsPlaying.value = true
    }

    override fun stopPreview() {
        previewPlayback.stop()
        if (_mode.value == PlaybackCoordinator.Mode.PREVIEW) _mode.value = PlaybackCoordinator.Mode.IDLE
        _previewIsPlaying.value = false
        _previewCurrentSong.value = null
    }

    override fun pausePreview() {
        previewPlayback.pause()
        _previewIsPlaying.value = false
    }

    override fun resumePreview() {
        if (_previewCurrentSong.value != null) {
            previewPlayback.resume()
            _previewIsPlaying.value = true
            _mode.value = PlaybackCoordinator.Mode.PREVIEW
        }
    }

    override fun togglePreview(song: Song) {
        val current = _previewCurrentSong.value
        if (current != null && current.songId == song.songId) {
            if (_previewIsPlaying.value) pausePreview() else resumePreview()
        } else {
            startPreview(song)
        }
    }

    override fun onFullPlaybackStarting() {
        pausePreview()
        _mode.value = PlaybackCoordinator.Mode.FULL
    }

    override fun onFullPlaybackStopped() {
        if (_mode.value == PlaybackCoordinator.Mode.FULL) _mode.value = PlaybackCoordinator.Mode.IDLE
    }
}


