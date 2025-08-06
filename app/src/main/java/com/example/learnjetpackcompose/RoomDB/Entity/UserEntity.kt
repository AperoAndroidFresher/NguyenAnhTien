package com.example.learnjetpackcompose.RoomDB.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val username: String,
    val password: String,
    val email: String,
    val displayName: String = "",
    val phoneNumber: String = "",
    val universityName: String ="",
    val description: String = "",
    val avatarPath: String =""
)