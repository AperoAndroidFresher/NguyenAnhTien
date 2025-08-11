package com.example.learnjetpackcompose.data.repository

import com.example.learnjetpackcompose.RoomDB.DAO.UserDao
import com.example.learnjetpackcompose.RoomDB.Entity.User
import com.example.learnjetpackcompose.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    override suspend fun getUserByUsername(username: String): User? {
        return userDao.getUser(username)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    override suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }
}


