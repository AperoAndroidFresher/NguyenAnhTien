package com.example.learnjetpackcompose.Screen.Library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.RoomDB.Entity.Playlist
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.data.repository.SongRepositoryImpl
import com.example.learnjetpackcompose.domain.repository.PlayerRepository
import com.example.learnjetpackcompose.domain.playback.PlaybackCoordinator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val songRepository: SongRepositoryImpl,
    private val playerRepository: PlayerRepository,
    private val playbackCoordinator: PlaybackCoordinator
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LibraryEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            playbackCoordinator.previewCurrentSong.collect { song ->
                _state.update { it.copy(currentPlayingSong = song) }
            }
        }
        viewModelScope.launch {
            playbackCoordinator.previewIsPlaying.collect { playing ->
                _state.update { it.copy(isPlaying = playing) }
            }
        }
    }

    fun updatePlaylists(playlists: List<Playlist>) {
        _state.update { it.copy(playlists = playlists) }
    }

    fun processIntent(intent: LibraryIntent) {
        when (intent) {
            is LibraryIntent.LoadSongs -> loadSongs(intent.songs)

            is LibraryIntent.SelectSource -> selectSource(intent.source)

            is LibraryIntent.AddSongToPlaylist -> addToPlaylist(intent.song)

            is LibraryIntent.LoadLocalSongs -> loadLocalSongs()

            is LibraryIntent.LoadRemoteSongs -> loadRemoteSongs()

            is LibraryIntent.ShareSong -> shareSong(intent.song)

            is LibraryIntent.DismissDialog -> dismissDialog()
            is LibraryIntent.PauseMusic -> pauseMusic()
            is LibraryIntent.PlaySong -> playSong(intent.song)
            is LibraryIntent.ResumeMusic -> resumeMusic()
            is LibraryIntent.StopMusic -> stopMusic()
            is LibraryIntent.UpdatePlaybackState -> updatePlaybackState(
                intent.isPlaying,
                intent.currentSong
            )
        }
    }

    private fun loadSongs(songs: List<Song>) {
        _state.update { currentState ->
            currentState.copy(
                songs = songs,
                filteredSongs = filterSongsBySource(songs, currentState.selectedSource),
                error = null
            )
        }
    }

    private fun selectSource(source: LibrarySource) {
        when (source) {
            LibrarySource.LOCAL -> loadLocalSongs()
            LibrarySource.REMOTE -> loadRemoteSongs()
        }
    }

    private fun addToPlaylist(song: Song) {
        _state.update {
            it.copy(
                showDialog = true,
                selectedSong = song
            )
        }
    }

    private fun dismissDialog() {
        _state.update {
            it.copy(
                showDialog = false,
                selectedSong = null
            )
        }
    }

    private fun shareSong(song: Song) {
        viewModelScope.launch {
            try {
                _effect.send(LibraryEffect.ShareSongFile(song))
            } catch (e: Exception) {
                _effect.send(LibraryEffect.ShowMessage("Failed to share song"))
            }
        }
    }

    private fun loadLocalSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update {
                    it.copy(
                        isLoading = true,
                        selectedSource = LibrarySource.LOCAL,
                        showDialog = false,
                        selectedSong = null
                    )
                }
                delay(500)

                val allSongs = _state.value.songs
                val localSongs = filterSongsBySource(allSongs, LibrarySource.LOCAL)

                _state.update {
                    it.copy(
                        isLoading = false,
                        filteredSongs = localSongs,
                        error = null
                    )
                }
                _effect.send(LibraryEffect.ShowMessage("Local songs loaded"))
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load local songs"
                    )
                }
            }
        }
    }

    private fun loadRemoteSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update {
                    it.copy(
                        isLoading = true,
                        selectedSource = LibrarySource.REMOTE,
                        showDialog = false,
                        selectedSong = null
                    )
                }
                delay(500)

                val remoteSongs = songRepository.getRemoteSongs()
                val currentLocalSongs = filterSongsBySource(_state.value.songs, LibrarySource.LOCAL)
                val allSongs = currentLocalSongs + remoteSongs

                _state.update {
                    it.copy(
                        isLoading = false,
                        songs = allSongs,
                        filteredSongs = remoteSongs,
                        error = null
                    )
                }
                _effect.send(LibraryEffect.ShowMessage("Remote songs loaded"))
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load remote songs"
                    )
                }
            }
        }
    }

    private fun filterSongsBySource(songs: List<Song>, source: LibrarySource): List<Song> {
        val result = when (source) {
            LibrarySource.LOCAL -> {
                songs.filter { !it.data.contains("/data/user/") && !it.data.contains("/files/songs") }
            }

            LibrarySource.REMOTE -> {
                songs.filter { it.data.contains("/files/songs") }
            }
        }
        return result
    }

    private fun playSong(song: Song) {
        viewModelScope.launch {
            playbackCoordinator.togglePreview(song)
            _state.update {
                it.copy(
                    isPlaying = playbackCoordinator.previewIsPlaying.value,
                    showPlayerBar = false, // preview mode: no global player bar
                    currentPlayingSong = song
                )
            }
        }
    }

    private fun pauseMusic() {
        viewModelScope.launch {
            _state.update { it.copy(isPlaying = false) }
        }
    }

    private fun resumeMusic() {
        viewModelScope.launch {
            _state.update { it.copy(isPlaying = true) }
        }
    }

    private fun stopMusic() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isPlaying = false,
                    showPlayerBar = false,
                    currentPlayingSong = null
                )
            }
        }
    }

    private fun updatePlaybackState(isPlaying: Boolean, currentSong: Song?) {
        _state.update { it.copy(isPlaying = isPlaying, currentPlayingSong = currentSong) }
    }
}