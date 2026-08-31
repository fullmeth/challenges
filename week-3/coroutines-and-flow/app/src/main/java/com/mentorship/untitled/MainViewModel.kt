package com.mentorship.untitled

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState(""))
    val uiState = _uiState.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiAction>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun loadScreenData() {
        _uiState.update { UiState("loaded") } // тут viewModelScope не потрібен, це не саспенд функція
    }

    fun pressLike() {
        viewModelScope.launch {
            _uiEvents.emit(UiAction.OnLikePressed)
        }
    }
}

data class UiState(val chatName: String)

sealed interface UiAction {
    data object OnLikePressed : UiAction
}