package com.example.dietplanner.dao

import androidx.room.*
import com.example.dietplanner.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users WHERE user_id = :id")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE email = :email AND password_hash = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?
    @Query("DELETE FROM users")
    suspend fun deleteAll()
}
