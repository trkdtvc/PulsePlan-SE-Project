package com.example.dietplanner.repository


import com.example.dietplanner.data.MealWithFood
import com.example.dietplanner.model.MealLog

interface MealLogRepository {
    suspend fun insert(mealLog: MealLog)
    suspend fun update(mealLog: MealLog)
    suspend fun delete(mealLog: MealLog)
    suspend fun getMealLogsByUser(userId: Int): List<MealLog>
    suspend fun getMealsWithFoodByUser(userId: Int): List<MealWithFood>
}
