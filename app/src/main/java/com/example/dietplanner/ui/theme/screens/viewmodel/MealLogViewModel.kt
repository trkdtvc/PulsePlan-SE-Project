package com.example.dietplanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dietplanner.data.MealWithFood
import com.example.dietplanner.model.MealLog
import com.example.dietplanner.repository.MealLogRepository
import com.example.dietplanner.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealLogViewModel @Inject constructor(
    private val mealLogRepository: MealLogRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _meals = MutableStateFlow<List<MealWithFood>>(emptyList())
    val meals: StateFlow<List<MealWithFood>> = _meals

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch {
            sessionManager.userIdFlow.filterNotNull().collect { userId ->
                try {
                    _meals.value = mealLogRepository.getMealsWithFoodByUser(userId)
                } catch (e: Exception) {
                    _error.value = "Failed to load meals: ${e.message}"
                }
            }
        }
    }

    fun addMealLog(foodId: Int, grams: Double) = viewModelScope.launch {
        try {
            val userId = sessionManager.userIdFlow.firstOrNull()
            if (userId != null) {
                val log = MealLog(user_id = userId, food_id = foodId, quantity_g = grams.toFloat())
                mealLogRepository.insert(log)
                _meals.value = mealLogRepository.getMealsWithFoodByUser(userId)
            } else {
                _error.value = "User not logged in"
            }
        } catch (e: Exception) {
            _error.value = "Failed to add meal: ${e.message}"
        }
    }
}
