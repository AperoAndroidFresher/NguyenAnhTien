package com.example.learnjetpackcompose.RoomDB.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.learnjetpackcompose.RoomDB.Entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("""
        SELECT * 
        FROM user_table 
        WHERE username = :username
    """)
    suspend fun getUser(username: String): User?

    @Query("""
        SELECT * 
        FROM user_table 
        WHERE email = :email
    """)
    suspend fun getUserByEmail(email: String): User?

    @Query("""
        SELECT * 
        FROM user_table 
        WHERE userId = :userId
    """)
    suspend fun getUserById(userId: Int): User?
}