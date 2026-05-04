package com.example.dietplanner.data

import androidx.room.ColumnInfo
import com.example.dietplanner.model.Food
import com.example.dietplanner.model.MealLog

data class MealWithFood(

    @ColumnInfo(name = "log_id") val id: Int,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "food_id") val foodId: Int,
    val quantity_g: Float,
    val food_name: String,
    val calories_per_100g: Float,
    val protein_g: Float,
    val fats_g: Float,
    val carbs_g: Float
)
