package com.example.dietplanner.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val user_id: Int = 0,
    val fullName: String,
    val username: String,
    val email: String,
    val password_hash: String,
    val height_cm: Float,
    val weight_kg: Float,
    val age: Int,
    val gender: String,
    val activity_level: String,
    val daily_calorie_goal: Float
)
