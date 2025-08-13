package com.example.learnjetpackcompose.di.qualifiers // Đặt trong cùng package hoặc package con của DI

import javax.inject.Qualifier
import kotlin.annotation.Retention
import kotlin.annotation.AnnotationRetention

/**
 * Qualifier for the Song API Retrofit instance.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY) // Chỉ ra rằng annotation này sẽ được giữ lại trong bytecode (thường dùng cho DI)
annotation class SongApi

/**
 * Qualifier for the Home API Retrofit instance.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY) // Đảm bảo Hilt có thể đọc được nó khi compile
annotation class HomeApi