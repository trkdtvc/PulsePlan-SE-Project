package com.example.dietplanner.repository


import com.example.dietplanner.dao.MealLogDao
import com.example.dietplanner.data.MealWithFood
import com.example.dietplanner.model.MealLog
import javax.inject.Inject

class MealLogRepositoryImpl @Inject constructor(
    private val dao: MealLogDao
) : MealLogRepository{
    override suspend fun insert(log: MealLog) {dao.insert(log)}
    override suspend fun update(log: MealLog) = dao.update(log)
    override suspend fun delete(log: MealLog) = dao.delete(log)
    override suspend fun getMealLogsByUser(userId: Int) = dao.getMealLogsByUser(userId)
    override suspend fun getMealsWithFoodByUser(userId: Int): List<MealWithFood>{
        return dao.getMealWithFoodByUser(userId)
    }
}
