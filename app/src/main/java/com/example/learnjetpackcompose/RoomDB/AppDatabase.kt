package com.example.learnjetpackcompose.RoomDB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.learnjetpackcompose.RoomDB.DAO.PlaylistDao
import com.example.learnjetpackcompose.RoomDB.DAO.SongDao
import com.example.learnjetpackcompose.RoomDB.DAO.UserDao
import com.example.learnjetpackcompose.RoomDB.Entity.Playlist
import com.example.learnjetpackcompose.RoomDB.Entity.User

@Database(entities = [User::class, Playlist::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun songDao() : SongDao
}