package com.example.learnjetpackcompose

object UserManager {
    private val userList = mutableListOf<User>()

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

    fun getUserList(): List<User> = userList.toList()

    fun isUserExist(username: String, password: String): Boolean {
        return userList.any { it.username == username && it.password == password }
    }

    fun isEmailExist(email: String): Boolean {
        return userList.any { it.email == email }
    }

    fun isUsernameExist(username: String): Boolean {
        return userList.any { it.username == username }
    }
}

