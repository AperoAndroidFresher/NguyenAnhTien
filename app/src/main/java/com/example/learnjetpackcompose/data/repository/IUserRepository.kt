package com.example.learnjetpackcompose.data.repository

import com.example.learnjetpackcompose.RoomDB.Entity.User

interface IUserRepository {
    suspend fun insertUser(user: User)
    suspend fun getUserByUsername(username: String): User?
    suspend fun getUserByEmail(email: String): User?
}