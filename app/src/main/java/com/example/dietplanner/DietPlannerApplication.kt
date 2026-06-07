package com.example.dietplanner

import android.app.Application
import android.util.Log
import com.example.dietplanner.database.AppDatabase
import com.example.dietplanner.model.Food
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class DietPlannerApplication : Application() {

    @Inject
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        seedDefaultFoods()
    }

    private fun seedDefaultFoods() {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                val foodCount = database.foodDao().getFoodCount()

                if (foodCount == 0) {
                    val defaultFoods = listOf(
                        Food(
                            food_name = "Banana",
                            calories_per_100g = 89,
                            protein_g = 1.1,
                            carbs_g = 22.8,
                            fats_g = 0.3,
                            food_type = "snack"
                        ),
                        Food(
                            food_name = "Apple",
                            calories_per_100g = 52,
                            protein_g = 0.3,
                            carbs_g = 14.0,
                            fats_g = 0.2,
                            food_type = "snack"
                        ),
                        Food(
                            food_name = "Chicken Breast",
                            calories_per_100g = 165,
                            protein_g = 31.0,
                            carbs_g = 0.0,
                            fats_g = 3.6,
                            food_type = "lunch"
                        ),
                        Food(
                            food_name = "Rice",
                            calories_per_100g = 130,
                            protein_g = 2.7,
                            carbs_g = 28.0,
                            fats_g = 0.3,
                            food_type = "lunch"
                        ),
                        Food(
                            food_name = "Egg",
                            calories_per_100g = 155,
                            protein_g = 13.0,
                            carbs_g = 1.1,
                            fats_g = 11.0,
                            food_type = "breakfast"
                        ),
                        Food(
                            food_name = "Oatmeal",
                            calories_per_100g = 389,
                            protein_g = 16.9,
                            carbs_g = 66.3,
                            fats_g = 6.9,
                            food_type = "breakfast"
                        ),
                        Food(
                            food_name = "Greek Yogurt",
                            calories_per_100g = 59,
                            protein_g = 10.0,
                            carbs_g = 3.6,
                            fats_g = 0.4,
                            food_type = "snack"
                        ),
                        Food(
                            food_name = "Salmon",
                            calories_per_100g = 208,
                            protein_g = 20.0,
                            carbs_g = 0.0,
                            fats_g = 13.0,
                            food_type = "dinner"
                        ),
                        Food(
                            food_name = "Potato",
                            calories_per_100g = 77,
                            protein_g = 2.0,
                            carbs_g = 17.0,
                            fats_g = 0.1,
                            food_type = "lunch"
                        ),
                        Food(
                            food_name = "Tuna",
                            calories_per_100g = 132,
                            protein_g = 28.0,
                            carbs_g = 0.0,
                            fats_g = 1.0,
                            food_type = "dinner"
                        )
                    )

                    defaultFoods.forEach { food ->
                        database.foodDao().insert(food)
                    }

                    Log.d("DietPlannerApp", "Default foods inserted successfully.")
                } else {
                    Log.d("DietPlannerApp", "Food table already contains data. Skipping seed.")
                }
            } catch (e: Exception) {
                Log.e("DietPlannerApp", "Error seeding default foods: ${e.message}", e)
            }
        }
    }
}