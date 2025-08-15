package com.example.learnjetpackcompose.Screen.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.data.model.UserManager
import com.example.learnjetpackcompose.di.qualifiers.HomeApi
import com.example.learnjetpackcompose.domain.repository.TopMusicRepository
import com.example.learnjetpackcompose.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @HomeApi private val topMusic: TopMusicRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    companion object {
        private const val API_TIMEOUT = 15000L
    }

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

                val userId = UserManager.getCurrentUserId()
                val user = userRepository.getUserById(userId)
                val displayName = user?.displayName ?: user?.username ?: ""
                val avatar = user?.avatarPath

                withTimeout(API_TIMEOUT) {

                    val topAlbumsDeferred = async {
                        topMusic.getTopAlbums(
                            "e65449d181214f936368984d4f4d4ae8",
                            "f9b593e6-4503-414c-99a0-46595ecd2e23"
                        )
                    }

                    val topTracksDeferred = async {
                        topMusic.getTopTracks(
                            "e65449d181214f936368984d4f4d4ae8",
                            "f9b593e6-4503-414c-99a0-46595ecd2e23"
                        )
                    }

                    val topArtistsDeferred = async {
                        topMusic.getTopArtists("e65449d181214f936368984d4f4d4ae8")
                    }

                    val topAlbumsAll = topAlbumsDeferred.await()

                    val topTracksAll = topTracksDeferred.await()

                    val topArtistAll = topArtistsDeferred.await()

                    val topAlbums = topAlbumsAll.topalbums.album.take(6)
                    val topTracks = topTracksAll.toptracks.track.take(5)
                    val topArtists = topArtistAll.artists.artist.take(5)

                    _state.update {
                        it.copy(
                            isLoading = false,
                            displayName = displayName,
                            userAvatar = avatar,
                            topAlbums = topAlbums,
                            topTracks = topTracks,
                            topArtists = topArtists,
                            allAlbums = topAlbumsAll.topalbums,
                            allTracks = topTracksAll.toptracks,
                            allArtists = topArtistAll.artists
                        )
                    }
                }
            } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
                _state.update { it.copy(isLoading = false, error = "Request timeout") }
                _effect.send(HomeEffect.ShowMessage("Request timeout. Please check your internet connection."))
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
                _effect.send(HomeEffect.ShowMessage("Failed to load home data: ${e.message}"))
            }
        }
    }

    private fun showAllAlbums() {
        _state.update { it.copy(topAlbums = it.allAlbums.album) }
    }

    private fun showAllTracks() {
        _state.update { it.copy(topTracks = it.allTracks.track) }
    }

    private fun showAllArtists() {
        _state.update { it.copy(topArtists = it.allArtists.artist) }
    }
}