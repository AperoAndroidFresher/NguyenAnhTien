package com.example.learnjetpackcompose.data.repository

import com.example.learnjetpackcompose.RoomDB.DAO.PlaylistDao
import com.example.learnjetpackcompose.RoomDB.Entity.Playlist

class PlaylistRepository (private val playlistDao: PlaylistDao) {
    suspend fun addPlaylist(playlist: Playlist) {
        playlistDao.upsertPlaylist(playlist)
    }

    suspend fun getPlaylistsForUser(userId: Int): List<Playlist> {
        return playlistDao.getPlaylistsForUser(userId)
    }

    suspend fun deletePlaylist(playlistId: Int) {
        playlistDao.deletePlaylist(playlistId)
    }
}