package com.example.dietplanner.ui.theme.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.dietplanner.ui.theme.screens.viewmodel.MealViewModel


@HiltViewModel
class ShareProgressViewModel @Inject constructor() : ViewModel() {

    private val _progressText = MutableStateFlow("Loading progress...")
    val progressText: StateFlow<String> = _progressText

    init {
        loadProgress()
    }

    private fun loadProgress() {
        _progressText.value = "I've burned  of my project! "
    }

    fun refreshProgress() {
        _progressText.value = "I've now completed 80%! "
    }
}
