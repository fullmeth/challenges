package com.mentorship.project

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mentorship.project.api.RetrofitService
import com.mentorship.project.utils.Result
import com.mentorship.project.utils.safeApiCall
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application = application) {

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(MainUiState.initial())
    val state = _state.asStateFlow()

    private val dummyPreferences = DummyPreferences(application)

    fun getResponse(code: Int, delay: Int? = null) {
        viewModelScope.launch {
            val result = safeApiCall { RetrofitService.dummyApiService().getDummyJson(code, delay) }
            when (result) {
                is Result.Error -> _events.send(UiEvent.ShowSnackbar(result.exception.message.orEmpty()))
                is Result.Success -> _events.send(UiEvent.ShowSnackbar(result.data.text))
            }
        }
    }

    fun savePreference(value: String) {
        try {
            when (_state.value.selectedPreferenceType) {
                SharedPreferenceType.STRING -> dummyPreferences.string = value
                SharedPreferenceType.INT -> dummyPreferences.int = value.toInt()
                SharedPreferenceType.LONG -> dummyPreferences.long = value.toLong()
                SharedPreferenceType.FLOAT -> dummyPreferences.float = value.toFloat()
                SharedPreferenceType.BOOLEAN -> dummyPreferences.boolean =
                    value.toBooleanStrict()

                SharedPreferenceType.STRING_SET -> dummyPreferences.stringSet =
                    value.split(", ").toSet()
            }
        } catch (e: Exception) {
            viewModelScope.launch {
                _events.send(UiEvent.ShowSnackbar("You picked the wrong type. ${e.message}"))
            }
        }
    }

    fun getPreference() {
        _state.update {
            it.copy(
                sharedPreferenceValue = when (it.selectedPreferenceType) {
                    SharedPreferenceType.STRING -> dummyPreferences.string
                    SharedPreferenceType.INT -> dummyPreferences.int.toString()
                    SharedPreferenceType.LONG -> dummyPreferences.long.toString()
                    SharedPreferenceType.FLOAT -> dummyPreferences.float.toString()
                    SharedPreferenceType.BOOLEAN -> dummyPreferences.boolean.toString()
                    SharedPreferenceType.STRING_SET -> dummyPreferences.stringSet.joinToString(", ")
                }
            )
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