package com.example.dietplanner.repository

import com.example.dietplanner.model.User

interface UserRepository {
    suspend fun insert(user: User): Long
    suspend fun update(user: User)
    suspend fun delete(user: User)
    suspend fun getUserById(id: Int): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?
}
