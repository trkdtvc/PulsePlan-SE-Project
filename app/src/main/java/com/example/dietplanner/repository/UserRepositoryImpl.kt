package com.example.dietplanner.repository

import com.example.dietplanner.dao.UserDao
import com.example.dietplanner.model.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao
) : UserRepository {
    override suspend fun insert(user: User): Long = dao.insert(user)
    override suspend fun update(user: User) = dao.update(user)
    override suspend fun delete(user: User) = dao.delete(user)
    override suspend fun getUserById(id: Int) = dao.getUserById(id)
    override suspend fun getUserByEmail(email: String) = dao.getUserByEmail(email)
    override suspend fun getUserByEmailAndPassword(email: String, password: String) =
        dao.getUserByEmailAndPassword(email, password)
}
