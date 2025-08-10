package com.example.learnjetpackcompose.Screen.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.data.model.UserManager
import com.example.learnjetpackcompose.data.repository.SongRepositoryImpl
import com.example.learnjetpackcompose.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val songRepository: SongRepositoryImpl,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _effect = Channel<HomeEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        processIntent(HomeIntent.LoadData)
    }

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadData -> loadData()
            HomeIntent.ShowAllAlbums -> showAllAlbums()
            HomeIntent.ShowAllTracks -> showAllTracks()
            HomeIntent.ShowAllArtists -> showAllArtists()
        }
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                // Fetch remote songs as a data source for Top Tracks; reuse to derive albums/artists
                val remoteSongs: List<Song> = songRepository.getRemoteSongs()

                val topTracksAll = remoteSongs
                val topTracks = topTracksAll.take(5)

                // Build simple derived data for albums (by title/artist) and artists
                val albumsAll = remoteSongs.map { song ->
                    HomeAlbum(
                        title = song.title,
                        artist = song.artist,
                        coverUri = song.albumArt
                    )
                }
                val albums = albumsAll.take(6)

                val artistsAll = remoteSongs
                    .map { it.artist to it.albumArt }
                    .distinctBy { it.first }
                    .map { (name, avatar) -> HomeArtist(name = name, avatarUri = avatar) }
                val artists = artistsAll.take(5)

                val userId = UserManager.getCurrentUserId()
                val user = userRepository.getUserById(userId)
                val displayName = user?.displayName ?: user?.username ?: ""
                val avatar = user?.avatarPath

                _state.update {
                    it.copy(
                        isLoading = false,
                        displayName = displayName,
                        userAvatar = avatar,
                        topAlbums = albums,
                        topTracks = topTracks,
                        topArtists = artists,
                        allAlbums = albumsAll,
                        allTracks = topTracksAll,
                        allArtists = artistsAll
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
                _effect.send(HomeEffect.ShowMessage("Failed to load home data"))
            }
        }
    }

    private fun showAllAlbums() {
        val albums = _state.value.allAlbums
        _state.update { it.copy(topAlbums = albums) }
    }

    private fun showAllTracks() {
        val tracks = _state.value.allTracks
        _state.update { it.copy(topTracks = tracks) }
    }

    private fun showAllArtists() {
        val artists = _state.value.allArtists
        _state.update { it.copy(topArtists = artists) }
    }
}


