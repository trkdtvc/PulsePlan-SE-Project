package com.example.dietplanner

import com.example.dietplanner.ui.theme.screens.calculateDailyCalories
import org.junit.Assert.assertEquals
import org.junit.Test

class DailyCalorieCalculationTest {

    @Test
    fun calculateDailyCalories_maleModerate_returnsExpectedGoal() {
        val result = calculateDailyCalories(
            weightKg = 80f,
            heightCm = 180f,
            age = 25,
            gender = "Male",
            activityLevel = "Moderate"
        )

        assertEquals(2797.75f, result, 0.1f)
    }

    @Test
    fun calculateDailyCalories_femaleSedentary_returnsExpectedGoal() {
        val result = calculateDailyCalories(
            weightKg = 65f,
            heightCm = 165f,
            age = 30,
            gender = "Female",
            activityLevel = "Sedentary"
        )

        assertEquals(1644.3f, result, 0.1f)
    }
}
