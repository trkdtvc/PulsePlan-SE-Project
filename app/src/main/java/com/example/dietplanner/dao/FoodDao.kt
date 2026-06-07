package com.example.dietplanner.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.dietplanner.model.Food

@Dao
interface FoodDao : BaseDao<Food> {

    @Query("SELECT * FROM food")
    suspend fun getAllFoods(): List<Food>

    @Query("SELECT * FROM food WHERE food_type = :type")
    suspend fun getFoodsByType(type: String): List<Food>

    @Query("SELECT COUNT(*) FROM food")
    suspend fun getFoodCount(): Int

    @Query("DELETE FROM food")
    suspend fun deleteAll()
}