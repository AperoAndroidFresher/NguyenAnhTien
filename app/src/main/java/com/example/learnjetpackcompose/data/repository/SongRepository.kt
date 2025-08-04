package com.example.learnjetpackcompose.data.repository

import com.example.learnjetpackcompose.RoomDB.DAO.SongDao
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongRepository @Inject constructor(
    private val songDao: SongDao
) : ISongRepository {

    override suspend fun getAllSongs(): List<Song> {
        // Placeholder implementation
        return emptyList()
    }
}
