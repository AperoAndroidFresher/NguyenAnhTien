package com.example.learnjetpackcompose.di

import android.content.Context
import com.example.learnjetpackcompose.data.playback.PlaybackCoordinatorImpl
import com.example.learnjetpackcompose.data.playback.PreviewPlayerImpl
import com.example.learnjetpackcompose.domain.playback.PlaybackCoordinator
import com.example.learnjetpackcompose.domain.playback.PreviewPlayback
import com.example.learnjetpackcompose.domain.repository.PlayerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaybackModule {

    @Provides
    @Singleton
    fun providePreviewPlayback(@ApplicationContext context: Context): PreviewPlayback = PreviewPlayerImpl(context)

    @Provides
    @Singleton
    fun providePlaybackCoordinator(
        previewPlayback: PreviewPlayback,
        playerRepository: PlayerRepository
    ): PlaybackCoordinator = PlaybackCoordinatorImpl(previewPlayback, playerRepository)
}


