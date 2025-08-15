package com.example.learnjetpackcompose.di

import com.example.learnjetpackcompose.data.repository.PlaylistRepositoryImpl
import com.example.learnjetpackcompose.data.repository.PlayerRepositoryImpl
import com.example.learnjetpackcompose.data.repository.SongRepositoryImpl
import com.example.learnjetpackcompose.data.repository.UserRepositoryImpl
import com.example.learnjetpackcompose.domain.playback.PlaybackGateway
import com.example.learnjetpackcompose.data.playback.PlaybackGatewayImpl
import com.example.learnjetpackcompose.data.repository.TopMusicRepositoryImpl
import com.example.learnjetpackcompose.di.qualifiers.HomeApi
import com.example.learnjetpackcompose.domain.repository.PlayerRepository
import com.example.learnjetpackcompose.domain.repository.PlaylistRepository
import com.example.learnjetpackcompose.domain.repository.SongRepository
import com.example.learnjetpackcompose.domain.repository.TopMusicRepository
import com.example.learnjetpackcompose.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserRepository(
        userRepository: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindPlaylistRepository(
        playlistRepository: PlaylistRepositoryImpl
    ): PlaylistRepository

    @Binds
    abstract fun bindSongRepository(
        songRepository: SongRepositoryImpl
    ): SongRepository

    @Binds
    abstract fun bindPlayerRepository(
        playerRepository: PlayerRepositoryImpl
    ): PlayerRepository

    @Binds
    abstract fun bindPlaybackGateway(
        playbackGatewayImpl: PlaybackGatewayImpl
    ): PlaybackGateway

    @HomeApi
    @Binds
    abstract fun bindTopMusicRepository(
        topMusicRepositoryImpl: TopMusicRepositoryImpl
    ): TopMusicRepository
}