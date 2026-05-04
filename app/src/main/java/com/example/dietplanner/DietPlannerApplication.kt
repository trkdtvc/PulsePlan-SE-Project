package com.example.dietplanner

import android.app.Application
import android.util.Log
import com.example.dietplanner.database.AppDatabase
import com.example.dietplanner.model.Food
import com.example.dietplanner.model.MealLog
import com.example.dietplanner.model.User
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class DietPlannerApplication : Application() {

    @Inject
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Očisti sve tabele za test
                database.userDao().deleteAll()
                database.foodDao().deleteAll()
                database.mealLogDao().deleteAll()

                //  test korisnik
                val userId = database.userDao().insert(
                    User(
                        username = "testuser",
                        email = "test@example.com",
                        password_hash = "hashedpass",
                        height_cm = 175f,
                        weight_kg = 70f,
                        age = 25,
                        gender = "M",
                        activity_level = "moderate",
                        daily_calorie_goal = 2500f,
                        fullName = "Test User"
                    )
                ).toInt()

                // test namirnice
                val bananaId = database.foodDao().insert(
                    Food(
                        food_name = "Banana",
                        calories_per_100g = 89,
                        protein_g = 1.1,
                        carbs_g = 22.8,
                        fats_g = 0.3,
                        food_type = "snack"
                    )
                ).toInt()

                val chickenId = database.foodDao().insert(
                    Food(
                        food_name = "Chicken Breast",
                        calories_per_100g = 165,
                        protein_g = 31.0,
                        carbs_g = 0.0,
                        fats_g = 3.6,
                        food_type = "lunch"
                    )
                ).toInt()

                //  par obroka za  korisnika
                database.mealLogDao().insert(
                    MealLog(
                        user_id = userId,
                        food_id = bananaId,
                        quantity_g = 150.0f
                    )
                )

                database.mealLogDao().insert(
                    MealLog(
                        user_id = userId,
                        food_id = chickenId,
                        quantity_g = 200.0f
                    )
                )

                Log.d("DietPlannerApp", "Test data inserted successfully!")

            } catch (e: Exception) {
                Log.e("DietPlannerApp", "Error inserting test data: ${e.message}", e)
            }
        }
    }
}
