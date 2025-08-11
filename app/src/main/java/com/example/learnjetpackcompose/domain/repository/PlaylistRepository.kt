package com.example.learnjetpackcompose.domain.repository

import com.example.learnjetpackcompose.RoomDB.Entity.Playlist

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun getPlaylistsForUser(userId: Int): List<Playlist>
    suspend fun deletePlaylist(playlistId: Int)
}