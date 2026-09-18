package com.mentorship.title.auth

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class AuthState(val user: MeResponse? = null, val error: String? = null)

class AuthViewModel(context: Application) : AndroidViewModel(context) {
    private val repository = AuthRepository(context)
    var state by mutableStateOf(AuthState())
        private set

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            state = state.copy(error = "Enter your username and password.")
            return
        }
        viewModelScope.launch {
            repository.login(username, password)
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            state = when (val me = repository.profile()) {
                is Result.Error -> state.copy(error = me.exception.message)
                is Result.Success -> state.copy(user = me.data)
            }
        }
    }
}
