package com.example.dietplanner.repository


import com.example.dietplanner.model.Food

interface FoodRepository {
    suspend fun insert(food: Food)
    suspend fun update(food: Food)
    suspend fun delete(food: Food)
    suspend fun getAllFoods(): List<Food>
    suspend fun getFoodsByType(type: String): List<Food>
}
