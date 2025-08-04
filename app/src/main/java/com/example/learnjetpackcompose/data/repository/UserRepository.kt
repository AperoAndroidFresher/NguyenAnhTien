package com.example.learnjetpackcompose.data.repository

import com.example.learnjetpackcompose.RoomDB.DAO.UserDao
import com.example.learnjetpackcompose.RoomDB.Entity.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) : IUserRepository {

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun getUserByUsername(username: String): User? {
        return userDao.getUser(username)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}