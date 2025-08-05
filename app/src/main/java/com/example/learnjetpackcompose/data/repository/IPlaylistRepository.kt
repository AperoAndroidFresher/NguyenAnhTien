package com.example.learnjetpackcompose.data.repository

import com.example.learnjetpackcompose.RoomDB.Entity.Playlist

interface IPlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun getPlaylistsForUser(userId: Int): List<Playlist>
    suspend fun deletePlaylist(playlistId: Int)
}