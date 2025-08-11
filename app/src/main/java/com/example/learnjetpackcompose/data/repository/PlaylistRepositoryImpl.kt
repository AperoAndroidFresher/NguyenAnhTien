package com.example.learnjetpackcompose.data.repository

import com.example.learnjetpackcompose.RoomDB.DAO.PlaylistDao
import com.example.learnjetpackcompose.RoomDB.Entity.Playlist
import com.example.learnjetpackcompose.domain.repository.PlaylistRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistDao.upsertPlaylist(playlist)
    }

    override suspend fun getPlaylistsForUser(userId: Int): List<Playlist> {
        return playlistDao.getPlaylistsForUser(userId)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistDao.deletePlaylist(playlistId)
    }
}


