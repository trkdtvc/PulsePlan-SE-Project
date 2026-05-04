package com.example.dietplanner.ui.theme.screens.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dietplanner.model.Food
import com.example.dietplanner.model.MealLog
import com.example.dietplanner.repository.FoodRepository
import com.example.dietplanner.repository.MealLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val mealLogRepository: MealLogRepository
) : ViewModel() {

    private val _foods = MutableStateFlow<List<Food>>(emptyList())
    val foods: StateFlow<List<Food>> = _foods

    private val _burnedCalories = MutableStateFlow(0f)
    val burnedCalories: StateFlow<Float> = _burnedCalories

    init {
        loadFoods()
    }

    private fun loadFoods() {
        viewModelScope.launch {
            val list = foodRepository.getAllFoods() // suspend function
            _foods.value = list
        }
    }

    fun addMeal(mealEntry: MealEntry, userId: Int) {
        viewModelScope.launch {
            val mealLog = MealLog(
                user_id = userId,
                food_id = mealEntry.food.food_id,
                quantity_g = mealEntry.quantityGrams.toFloat(),
                log_date = System.currentTimeMillis()
            )
            mealLogRepository.insert(mealLog)
        }
    }
    fun setCaloriesBurned(calories: Float) {
        _burnedCalories.value = calories
    }

}
