package com.example.learnjetpackcompose.domain.repository

import com.example.learnjetpackcompose.RoomDB.Entity.User

interface UserRepository {
    suspend fun insertUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun getUserByUsername(username: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun getUserById(userId: Int): User?
}