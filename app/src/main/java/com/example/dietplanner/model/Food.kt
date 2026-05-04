package com.example.dietplanner.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food")
data class Food(
    @PrimaryKey(autoGenerate = true) val food_id: Int = 0,
    val food_name: String,
    val calories_per_100g: Int,
    val protein_g: Double,
    val carbs_g: Double,
    val fats_g: Double,
    val food_type: String
)
