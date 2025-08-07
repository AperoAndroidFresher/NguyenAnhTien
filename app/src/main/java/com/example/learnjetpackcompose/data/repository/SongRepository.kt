package com.example.learnjetpackcompose.data.repository

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.example.learnjetpackcompose.RoomDB.DAO.SongDao
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.Utils.Mp3Downloader
import com.example.learnjetpackcompose.data.api.ApiService
import com.example.learnjetpackcompose.data.api.RemoteSongDto
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.absoluteValue

@Singleton
class SongRepository @Inject constructor(
    private val songDao: SongDao,
    private val apiService: ApiService,
    private val downloader: Mp3Downloader
) : ISongRepository {

    override suspend fun getAllSongs(): List<Song> {

        return emptyList()
    }

    suspend fun getRemoteSongs(): List<Song> {
        return try {
            val response = apiService.getRemoteSongs()
            if (response.isSuccessful) {
                val remoteSongs = response.body() ?: return emptyList()
                val localSongs = mutableListOf<Song>()

                for (remoteSong in remoteSongs) {
                    localSongs.add(remoteSong.toSong())
                }
                localSongs

            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun RemoteSongDto.toSong(): Song {
        val downloadedFile = downloader.download(
            remoteUrl = this.path,
            rawFileName = this.title
        )
        val albumArtUri = extractAlbumArtAsUri(downloader.context, downloadedFile.absolutePath, this.path.hashCode().toLong().absoluteValue)
        return Song(
            songId = this.path.hashCode().toLong().absoluteValue,
            title = this.title,
            artist = this.artist,
            albumArt = albumArtUri.toString(),
            duration = formatDuration(this.duration),
            data = downloadedFile.absolutePath
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

    private fun extractAlbumArtAsUri(context: Context, audioPath: String, songId: Long): Uri? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(audioPath)
            val artBytes = retriever.embeddedPicture

            if (artBytes != null) {
                val cacheDir = File(context.cacheDir, "temp_album_arts")
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs()
                }

                val tempFile = File(cacheDir, "temp_art_$songId.jpg")
                tempFile.writeBytes(artBytes)
                Uri.fromFile(tempFile)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            try {
                retriever.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
