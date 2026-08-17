package com.mentorship.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentorship.project.api.DummyDto
import com.mentorship.project.utils.Result
import com.mentorship.project.utils.recover
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel(), Repository by repository {

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(MainUiState.initial())
    val state = _state.asStateFlow()

    fun getResponse(code: Int, delay: Int? = null) {
        viewModelScope.launch {
            val event = when (val result = repository.getDummyResponse(code, delay)) {
                is Result.Error -> UiEvent.ShowSnackbar("Error? But its not possible!")
                is Result.Success -> UiEvent.ShowSnackbar(result.data.message)
            }
            _events.send(event)
        }
    }

    fun savePreference(value: String) {
        try {
            repository.savePreference(_state.value.selectedPreferenceType, value)
        } catch (e: Exception) {
            viewModelScope.launch {
                _events.send(UiEvent.ShowSnackbar("You picked the wrong type. ${e.message}"))
            }
        }
    }

    fun getPreference() {
        _state.update {
            it.copy(sharedPreferenceValue = repository.getPreference(_state.value.selectedPreferenceType))
        }
    }

    fun selectSharedPreferenceType(type: SharedPreferenceType) {
        _state.update { it.copy(selectedPreferenceType = type) }
    }
}

data class MainUiState(
    val selectedPreferenceType: SharedPreferenceType,
    val sharedPreferenceValue: String,
) {
    companion object {
        fun initial() = MainUiState(SharedPreferenceType.STRING, "")
    }
}

enum class SharedPreferenceType {
    STRING,
    INT,
    LONG,
    FLOAT,
    BOOLEAN,
    STRING_SET,
}

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
}