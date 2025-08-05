package com.example.learnjetpackcompose.data.repository

import com.example.learnjetpackcompose.RoomDB.Entity.Song

interface ISongRepository {
    // Placeholder interface - có thể thêm methods sau
    suspend fun getAllSongs(): List<Song>
}