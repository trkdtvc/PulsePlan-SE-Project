package com.example.dietplanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dietplanner.model.User
import com.example.dietplanner.repository.UserRepository
import com.example.dietplanner.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val userIdFlow: Flow<Int?> = sessionManager.userIdFlow

    init {
        viewModelScope.launch {
            sessionManager.userIdFlow.collect { id ->
                if (id != null) {
                    loadUserById(id)
                }
            }
        }
    }

    fun register(user: User) = viewModelScope.launch {
        try {
            val id = userRepository.insert(user)
            sessionManager.saveUserId(id.toInt())
            _user.value = user.copy(user_id = id.toInt())
        } catch (e: Exception) {
            _error.value = "Registration failed: ${e.message}"
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        try {
            val user = userRepository.getUserByEmailAndPassword(email, password)
            if (user != null) {
                sessionManager.saveUserId(user.user_id)
                _user.value = user
            } else {
                _error.value = "Invalid email or password"
            }
        } catch (e: Exception) {
            _error.value = "Login failed: ${e.message}"
        }
    }

    fun loadUserById(id: Int) = viewModelScope.launch {
        try {
            _user.value = userRepository.getUserById(id)
        } catch (e: Exception) {
            _error.value = "Failed to load user: ${e.message}"
        }
    }

    fun logout() = viewModelScope.launch {
        sessionManager.clearSession()
        _user.value = null
    }
}

