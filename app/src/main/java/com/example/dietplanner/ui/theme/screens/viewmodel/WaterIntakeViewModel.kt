package com.example.dietplanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dietplanner.session.WaterIntakeManager
import com.example.dietplanner.session.WaterIntakeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaterIntakeViewModel @Inject constructor(
    private val waterIntakeManager: WaterIntakeManager
) : ViewModel() {

    val waterState: StateFlow<WaterIntakeState> =
        waterIntakeManager.waterStateFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WaterIntakeState()
        )

    fun addWater(amountMl: Int = 250) = viewModelScope.launch {
        waterIntakeManager.addWater(amountMl)
    }

    fun removeWater(amountMl: Int = 250) = viewModelScope.launch {
        waterIntakeManager.removeWater(amountMl)
    }

    fun resetWater() = viewModelScope.launch {
        waterIntakeManager.resetWater()
    }

    fun setGoal(goalMl: Int) = viewModelScope.launch {
        waterIntakeManager.setGoal(goalMl)
    }
}