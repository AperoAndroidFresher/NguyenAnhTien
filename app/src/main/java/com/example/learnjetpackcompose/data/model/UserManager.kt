package com.example.learnjetpackcompose.data.model

import com.example.learnjetpackcompose.RoomDB.Entity.User

object UserManager {
    private val userList = mutableListOf<User>( User(username ="ad", email = "admin@admin.com", password = "Abc123!"))

    private var currentUserId: Int? = null

    fun setCurrentUserId(userId: Int) {
        currentUserId = userId
    }

    fun getCurrentUserId(): Int {
        return currentUserId ?: 1
    }

    fun clearCurrentUserId() {
        currentUserId = null
    }

    fun addUser(user: User): Boolean {
        if (userList.any { it.username == user.username }) {
            return false
        }
        if (userList.any { it.email == user.email }) {
            return false
        }
        userList.add(user)
        return true
    }


    fun isEmailExist(email: String): Boolean {
        return userList.any { it.email == email }
    }

    fun isUsernameExist(username: String): Boolean {
        return userList.any { it.username == username }
    }
}

