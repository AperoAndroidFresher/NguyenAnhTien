package com.example.learnjetpackcompose.RoomDB.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnjetpackcompose.RoomDB.Entity.Playlist

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlaylist(playlist: Playlist)

    @Query("SELECT * FROM playlist_table WHERE userId = :userId")
    suspend fun getPlaylistsForUser(userId: Int): List<Playlist>

    @Query("DELETE FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)


}