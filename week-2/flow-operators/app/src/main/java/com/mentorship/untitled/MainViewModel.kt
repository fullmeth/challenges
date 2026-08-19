@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.mentorship.untitled

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentorship.untitled.api.RetrofitService
import com.mentorship.untitled.api.toSearchResultList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.milliseconds

class MainViewModel : ViewModel() {

    private val retrofit = RetrofitService.dummyApiService()

    private val queries = MutableSharedFlow<String>(1)

    val searchResultsState = queries
        .debounce(300.milliseconds)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            flow { emit(retrofit.search(query, 2500).toSearchResultList()) }
        }
        .stateIn(viewModelScope, WhileSubscribed(5_000), emptyList())

//    val searchResultsState = queries
//        .debounce(300.milliseconds)
//        .distinctUntilChanged()
//        .flatMapMerge { query ->
//            flow { emit(retrofit.search(query, 2500).toSearchResultList()) }
//        }
//        .stateIn(viewModelScope, WhileSubscribed(5_000), emptyList())

    fun onNewSearchValue(value: String) {
        queries.tryEmit(value)
    }
}

data class SearchResult(
    val title: String,
    val description: String,
)