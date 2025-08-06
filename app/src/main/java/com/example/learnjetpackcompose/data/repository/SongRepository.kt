package com.example.learnjetpackcompose.data.repository

import com.example.learnjetpackcompose.RoomDB.DAO.SongDao
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.data.api.ApiService
import com.example.learnjetpackcompose.data.api.RemoteSongDto
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.absoluteValue

@Singleton
class SongRepository @Inject constructor(
    private val songDao: SongDao,
    private val apiService: ApiService
) : ISongRepository {

    override suspend fun getAllSongs(): List<Song> {
        // Placeholder implementation
        return emptyList()
    }

    suspend fun getRemoteSongs(): List<Song> {
        return try {
            val response = apiService.getRemoteSongs()
            if (response.isSuccessful) {
                response.body()?.map { it.toSong() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun RemoteSongDto.toSong(): Song {
        return Song(
            songId = this.path.hashCode().toLong().absoluteValue,
            title = this.title,
            artist = this.artist,
            albumArt = null,
            duration = formatDuration(this.duration),
            data = this.path
        )
    }

    private fun formatDuration(durationMs: String): String {
        return try {
            val millis = durationMs.toLong()
            val minutes = (millis / 1000) / 60
            val seconds = (millis / 1000) % 60
            String.format("%d:%02d", minutes, seconds)
        } catch (e: Exception) {
            "0:00"
        }
    }
}
