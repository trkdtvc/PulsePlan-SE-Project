package com.example.dietplanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dietplanner.model.Food
import com.example.dietplanner.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _foods = MutableStateFlow<List<Food>>(emptyList())
    val foods: StateFlow<List<Food>> = _foods

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadAllFoods() = viewModelScope.launch {
        try {
            _foods.value = foodRepository.getAllFoods()
        } catch (e: Exception) {
            _error.value = "Failed to load foods: ${e.message}"
        }
    }

    fun loadFoodsByType(type: String) = viewModelScope.launch {
        try {
            _foods.value = foodRepository.getFoodsByType(type)
        } catch (e: Exception) {
            _error.value = "Failed to filter foods"
        }
    }
}
