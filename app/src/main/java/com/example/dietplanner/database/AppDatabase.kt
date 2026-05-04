package com.example.dietplanner.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import com.example.dietplanner.dao.FoodDao
import com.example.dietplanner.dao.MealLogDao
import com.example.dietplanner.dao.UserDao
import com.example.dietplanner.model.Food
import com.example.dietplanner.model.MealLog
import com.example.dietplanner.model.User


@Database(
    entities = [User::class, Food::class, MealLog::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun foodDao(): FoodDao
    abstract fun mealLogDao(): MealLogDao

}
