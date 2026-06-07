package com.example.dietplanner

import com.example.dietplanner.dao.UserDao
import com.example.dietplanner.model.User
import com.example.dietplanner.repository.UserRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class UserRepositoryImplTest {

    private class FakeUserDao : UserDao {
        private val users = mutableMapOf<Int, User>()
        private var nextId = 1

        override suspend fun insert(user: User): Long {
            val id = nextId++
            users[id] = user.copy(user_id = id)
            return id.toLong()
        }

        override suspend fun update(user: User) {
            users[user.user_id] = user
        }

        override suspend fun delete(user: User) {
            users.remove(user.user_id)
        }

        override suspend fun getUserById(id: Int): User? = users[id]

        override suspend fun getUserByEmail(email: String): User? =
            users.values.firstOrNull { it.email == email }

        override suspend fun getUserByEmailAndPassword(email: String, password: String): User? =
            users.values.firstOrNull { it.email == email && it.password_hash == password }

        override suspend fun deleteAll() {
            users.clear()
        }
    }

    @Test
    fun updateProfile_savesEditedUserValues() = runBlocking {
        val repository = UserRepositoryImpl(FakeUserDao())
        val id = repository.insert(
            User(
                fullName = "Test User",
                username = "testuser",
                email = "test@stu.ibu.edu.ba",
                password_hash = "password123",
                height_cm = 180f,
                weight_kg = 80f,
                age = 21,
                gender = "Male",
                activity_level = "Moderate",
                daily_calorie_goal = 2200f
            )
        ).toInt()

        val updatedUser = repository.getUserById(id)!!.copy(
            age = 22,
            height_cm = 181f,
            weight_kg = 82f,
            activity_level = "Active"
        )

        repository.update(updatedUser)

        val savedUser = repository.getUserById(id)
        assertNotNull(savedUser)
        assertEquals(22, savedUser!!.age)
        assertEquals(181f, savedUser.height_cm, 0.01f)
        assertEquals(82f, savedUser.weight_kg, 0.01f)
        assertEquals("Active", savedUser.activity_level)
    }
}
