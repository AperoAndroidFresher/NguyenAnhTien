package com.example.learnjetpackcompose.domain.repository

import com.example.learnjetpackcompose.RoomDB.Entity.Song

interface SongRepository {

    suspend fun getAllSongs(): List<Song>
}