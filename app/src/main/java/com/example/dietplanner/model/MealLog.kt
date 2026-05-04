package com.example.dietplanner.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "meal_Log",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["user_id"], childColumns = ["user_id"]),
        ForeignKey(entity = Food::class, parentColumns = ["food_id"], childColumns = ["food_id"])
    ]
)
data class MealLog(
    @PrimaryKey(autoGenerate = true) val log_id: Int = 0,
    val user_id: Int,
    val food_id: Int,
    val quantity_g: Float,
    val log_date: Long = System.currentTimeMillis()
)

