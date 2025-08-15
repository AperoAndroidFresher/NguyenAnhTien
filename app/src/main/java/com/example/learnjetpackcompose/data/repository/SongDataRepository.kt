package com.example.learnjetpackcompose.data.repository

import android.content.Context
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import getAllMp3FilesOptimized
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongDataRepository @Inject constructor(
    private val context: Context,
    private val songRepository: SongRepositoryImpl
) {
    private var _localSongs: List<Song> = emptyList()
    private var _remoteSongs: List<Song> = emptyList()
    private var _allSongs: List<Song> = emptyList()


    fun getAllSongs(): List<Song> = _allSongs
    fun getLocalSongs(): List<Song> = _localSongs
    fun getRemoteSongs(): List<Song> = _remoteSongs

    suspend fun refreshAllSongs(): List<Song> = withContext(Dispatchers.IO) {
        try {
            val localDeferred = async { refreshLocalSongsInternal() }
            val remoteDeferred = async { refreshRemoteSongsInternal() }

            _localSongs = localDeferred.await()
            _remoteSongs = remoteDeferred.await()
            mergeSongs()
            _allSongs
        } catch (e: Exception) {
            _allSongs
        }
    }

    suspend fun refreshLocalSongs(): List<Song> = withContext(Dispatchers.IO) {
        try {
            _localSongs = refreshLocalSongsInternal()
            mergeSongs()
            _allSongs
        } catch (e: Exception) {
            _allSongs
        }
    }

    suspend fun refreshRemoteSongs(): List<Song> = withContext(Dispatchers.IO) {
        try {
            _remoteSongs = refreshRemoteSongsInternal()
            mergeSongs()
            _allSongs
        } catch (e: Exception) {
            _allSongs
        }
    }

    suspend fun initialize(): List<Song> = withContext(Dispatchers.IO) {
        try {
            _localSongs = getAllMp3FilesOptimized(context)
            _allSongs = _localSongs
            _allSongs
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun refreshLocalSongsInternal(): List<Song> {
        return getAllMp3FilesOptimized(context)
    }

    private suspend fun refreshRemoteSongsInternal(): List<Song> {
        return songRepository.getRemoteSongs()
    }

    private fun mergeSongs() {
        _allSongs = _localSongs + _remoteSongs
    }

    fun filterSongsBySource(source: String): List<Song> {
        return when (source) {
            "LOCAL" -> _localSongs
            "REMOTE" -> _remoteSongs
            else -> _allSongs
        }
    }
}
