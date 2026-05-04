package com.example.dietplanner.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.dietplanner.data.MealWithFood
import com.example.dietplanner.model.MealLog

@Dao
interface MealLogDao : BaseDao<MealLog> {
    @Query("SELECT * FROM meal_log ml WHERE user_id = :userId")
    suspend fun getMealLogsByUser(userId: Int): List<MealLog>
    @Query("""
        SELECT ml.log_id, ml.user_id, ml.food_id, ml.quantity_g, ml.log_date,
               f.food_name, f.calories_per_100g, f.protein_g, f.carbs_g, f.fats_g, f.food_type
        FROM meal_log ml
        INNER JOIN food f ON ml.food_id = f.food_id
        WHERE ml.user_id = :userId
        ORDER BY ml.log_date DESC
    """)
    suspend fun getMealWithFoodByUser(userId: Int): List<MealWithFood>

    @Query("DELETE FROM meal_log")
    suspend fun deleteAll()
}
