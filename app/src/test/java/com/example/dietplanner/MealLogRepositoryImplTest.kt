package com.example.dietplanner

import com.example.dietplanner.dao.MealLogDao
import com.example.dietplanner.data.MealWithFood
import com.example.dietplanner.model.MealLog
import com.example.dietplanner.repository.MealLogRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MealLogRepositoryImplTest {

    private class FakeMealLogDao : MealLogDao {
        private val logs = mutableListOf<MealLog>()

        override suspend fun insert(entity: MealLog): Long {
            logs.add(entity)
            return entity.log_id.toLong()
        }

        override suspend fun update(entity: MealLog) {
            val index = logs.indexOfFirst { it.log_id == entity.log_id }
            if (index != -1) logs[index] = entity
        }

        override suspend fun delete(entity: MealLog) {
            logs.removeAll { it.log_id == entity.log_id }
        }

        override suspend fun deleteMealLogById(mealLogId: Int, userId: Int) {
            logs.removeAll { it.log_id == mealLogId && it.user_id == userId }
        }

        override suspend fun getMealLogsByUser(userId: Int): List<MealLog> =
            logs.filter { it.user_id == userId }

        override suspend fun getMealWithFoodByUser(userId: Int): List<MealWithFood> =
            logs.filter { it.user_id == userId }.map {
                MealWithFood(
                    id = it.log_id,
                    userId = it.user_id,
                    foodId = it.food_id,
                    quantity_g = it.quantity_g,
                    food_name = "Test Food",
                    calories_per_100g = 100f,
                    protein_g = 10f,
                    fats_g = 5f,
                    carbs_g = 15f
                )
            }

        override suspend fun deleteAll() {
            logs.clear()
        }
    }

    @Test
    fun deleteMealLogById_removesOnlySelectedUsersMeal() = runBlocking {
        val repository = MealLogRepositoryImpl(FakeMealLogDao())

        repository.insert(MealLog(log_id = 1, user_id = 1, food_id = 10, quantity_g = 150f))
        repository.insert(MealLog(log_id = 2, user_id = 1, food_id = 11, quantity_g = 200f))
        repository.insert(MealLog(log_id = 3, user_id = 2, food_id = 12, quantity_g = 250f))

        repository.deleteMealLogById(mealLogId = 2, userId = 1)

        val userOneMeals = repository.getMealLogsByUser(1)
        val userTwoMeals = repository.getMealLogsByUser(2)

        assertEquals(1, userOneMeals.size)
        assertEquals(1, userOneMeals.first().log_id)
        assertEquals(1, userTwoMeals.size)
        assertTrue(userTwoMeals.any { it.log_id == 3 })
    }
}
