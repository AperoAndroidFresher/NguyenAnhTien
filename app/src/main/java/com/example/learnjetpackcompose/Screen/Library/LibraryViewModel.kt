package com.example.learnjetpackcompose.Screen.Library

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.RoomDB.Entity.Playlist
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.Utils.hasStoragePermission
import com.example.learnjetpackcompose.data.repository.SongDataRepository
import com.example.learnjetpackcompose.domain.repository.PlayerRepository
import com.example.learnjetpackcompose.domain.playback.PlaybackCoordinator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val songDataRepository: SongDataRepository,
    private val playerRepository: PlayerRepository,
    private val playbackCoordinator: PlaybackCoordinator,
    @ApplicationContext private val context: Context
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
        processIntent(LibraryIntent.CheckStoragePermission)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(isLoading = true) }
                songDataRepository.initialize()
                viewModelScope.launch(Dispatchers.IO) { processIntent(LibraryIntent.LoadLocalSongs) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Failed to initialize songs") }
            }
        }
    }

    fun updatePlaylists(playlists: List<Playlist>) {
        _state.update { it.copy(playlists = playlists) }
    }

    fun checkPermissionOnEntry() {
        if (!context.hasStoragePermission()) {
            _state.update { it.copy(showPermissionDialog = true) }
        }
    }

    fun processIntent(intent: LibraryIntent) {
        when (intent) {
            is LibraryIntent.LoadSongs -> loadSongs(intent.songs)

            is LibraryIntent.SelectSource -> selectSource(intent.source)

            is LibraryIntent.AddSongToPlaylist -> addToPlaylist(intent.song)

            is LibraryIntent.LoadLocalSongs -> loadLocalSongs()

            is LibraryIntent.LoadRemoteSongs -> loadRemoteSongs()
            is LibraryIntent.RefreshAllSongs -> refreshAllSongs()

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
            is LibraryIntent.CheckStoragePermission -> checkStoragePermission()
            is LibraryIntent.RequestStoragePermission -> requestStoragePermission()
            is LibraryIntent.DismissPermissionDialog -> dismissPermissionDialog()
            is LibraryIntent.OnPermissionGranted -> onPermissionGranted()
        }
    }

    private fun loadSongs(songs: List<Song>) {
        _state.update { currentState ->
            currentState.copy(
                songs = songs,
                filteredSongs = when (currentState.selectedSource) {
                    LibrarySource.LOCAL -> songDataRepository.getLocalSongs()
                    LibrarySource.REMOTE -> songDataRepository.getRemoteSongs()
                },
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
        _state.update {
            it.copy(
                selectedSource = LibrarySource.LOCAL,
                isLoading = false,
                filteredSongs = songDataRepository.getLocalSongs(),
                error = null,
                showDialog = false,
                selectedSong = null
            )
        }
    }

    private fun loadRemoteSongs() {
        viewModelScope.launch {
            try {
                val cachedRemoteSongs = songDataRepository.getRemoteSongs()
                if (cachedRemoteSongs.isNotEmpty()) {
                    _state.update {
                        it.copy(
                            selectedSource = LibrarySource.REMOTE,
                            isLoading = false,
                            filteredSongs = cachedRemoteSongs,
                            error = null,
                            showDialog = false,
                            selectedSong = null
                        )
                    }
                    return@launch
                }
                _state.update {
                    it.copy(
                        selectedSource = LibrarySource.REMOTE,
                        isLoading = true,
                        showDialog = false,
                        selectedSong = null
                    )
                }
                songDataRepository.refreshRemoteSongs()

                _state.update {
                    it.copy(
                        isLoading = false,
                        filteredSongs = songDataRepository.getRemoteSongs(),
                        error = null
                    )
                }
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

    private fun checkStoragePermission() {
        if (!context.hasStoragePermission()) {
            _state.update { it.copy(showPermissionDialog = true) }
        }
    }

    private fun requestStoragePermission() {
        viewModelScope.launch {
            _effect.send(LibraryEffect.RequestStoragePermission)
            _state.update { it.copy(showPermissionDialog = false) }
        }
    }

    private fun dismissPermissionDialog() {
        _state.update { it.copy(showPermissionDialog = false) }
    }

    private fun refreshAllSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                songDataRepository.refreshAllSongs()
                // After refresh, update filtered list based on selected source
                viewModelScope.launch {
                    when (_state.value.selectedSource) {
                        LibrarySource.LOCAL -> loadLocalSongs()
                        LibrarySource.REMOTE -> loadRemoteSongs()
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to refresh songs") }
            }
        }
    }

    fun refreshLocalSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                songDataRepository.refreshLocalSongs()
                if (_state.value.selectedSource == LibrarySource.LOCAL) {
                    viewModelScope.launch { loadLocalSongs() }
                }
            } catch (_: Exception) { }
        }
    }

    private fun onPermissionGranted() {
        if (context.hasStoragePermission()) {
            viewModelScope.launch(Dispatchers.IO) {
                songDataRepository.refreshLocalSongs()
                viewModelScope.launch {
                    _state.update { it.copy(selectedSource = LibrarySource.LOCAL) }
                    loadLocalSongs()
                }
            }
        }
    }

    private fun updatePlaybackState(isPlaying: Boolean, currentSong: Song?) {
        _state.update { it.copy(isPlaying = isPlaying, currentPlayingSong = currentSong) }
    }
}