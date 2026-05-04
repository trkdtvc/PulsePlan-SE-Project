package com.example.dietplanner.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.dietplanner.model.Food

@Dao
interface FoodDao : BaseDao<Food> {
    @Query("SELECT * FROM food")
    suspend fun getAllFoods(): List<Food>
    @Query("SELECT * FROM Food WHERE food_type = :type")
    suspend fun getFoodsByType(type: String): List<Food>

    @Query("DELETE FROM food")
    suspend fun deleteAll()
}
