package com.example.learnjetpackcompose.domain.usecase

import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.data.repository.SongDataRepository
import javax.inject.Inject

class GetAllSongsUseCase @Inject constructor(
    private val songDataRepository: SongDataRepository
) {
    suspend operator fun invoke(): List<Song> {
        return songDataRepository.refreshAllSongs()
    }
}

class GetLocalSongsUseCase @Inject constructor(
    private val songDataRepository: SongDataRepository
) {
    suspend operator fun invoke(): List<Song> {
        return songDataRepository.refreshLocalSongs()
    }
}

class GetRemoteSongsUseCase @Inject constructor(
    private val songDataRepository: SongDataRepository
) {
    suspend operator fun invoke(): List<Song> {
        return songDataRepository.refreshRemoteSongs()
    }
}

class FilterSongsBySourceUseCase @Inject constructor(
    private val songDataRepository: SongDataRepository
) {
    operator fun invoke(source: String): List<Song> {
        return songDataRepository.filterSongsBySource(source)
    }
}

class InitializeSongsUseCase @Inject constructor(
    private val songDataRepository: SongDataRepository
) {
    suspend operator fun invoke(): List<Song> {
        return songDataRepository.initialize()
    }
}
