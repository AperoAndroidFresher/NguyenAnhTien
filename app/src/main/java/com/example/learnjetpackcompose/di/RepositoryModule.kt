package com.example.learnjetpackcompose.di

import com.example.learnjetpackcompose.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserRepository(
        userRepository: UserRepository
    ): IUserRepository

    @Binds
    abstract fun bindPlaylistRepository(
        playlistRepository: PlaylistRepository
    ): IPlaylistRepository

    @Binds
    abstract fun bindSongRepository(
        songRepository: SongRepository
    ): ISongRepository
}