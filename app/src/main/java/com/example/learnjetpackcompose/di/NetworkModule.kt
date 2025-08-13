package com.example.learnjetpackcompose.di

import android.content.Context
import com.example.learnjetpackcompose.Utils.Mp3Downloader
import com.example.learnjetpackcompose.data.api.ApiService
import com.example.learnjetpackcompose.di.qualifiers.HomeApi
import com.example.learnjetpackcompose.di.qualifiers.SongApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @SongApi
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://static.apero.vn/techtrek/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @HomeApi
    @Provides
    @Singleton
    fun provideHomeRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @HomeApi
    @Provides
    @Singleton
    fun provideHomeApiService(@HomeApi retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @SongApi
    @Provides
    @Singleton
    fun provideSongApiService(@SongApi retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMp3Downloader(
        @ApplicationContext context: Context
    ): Mp3Downloader {
        return Mp3Downloader(context)
    }
}