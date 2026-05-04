package com.example.dietplanner.repository


import com.example.dietplanner.dao.FoodDao
import com.example.dietplanner.model.Food
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    private val dao: FoodDao
) : FoodRepository {
    override suspend fun insert(food: Food) {dao.insert(food)}
    override suspend fun update(food: Food) = dao.update(food)
    override suspend fun delete(food: Food) = dao.delete(food)
    override suspend fun getAllFoods() = dao.getAllFoods()
    override suspend fun getFoodsByType(type: String) = dao.getFoodsByType(type)
}
