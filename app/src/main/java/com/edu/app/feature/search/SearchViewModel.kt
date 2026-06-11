package com.edu.app.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.app.domain.model.SearchResults
import com.edu.app.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val results: SearchResults = SearchResults.EMPTY,
    val hasSearched: Boolean = false
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val search: SearchUseCase
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val searching = MutableStateFlow(false)

    private val results: StateFlow<SearchResults> = query
        .debounce(280)
        .map { it.trim() }
        .distinctUntilChanged()
        .flatMapLatest { q ->
            flow {
                if (q.length < 2) emit(SearchResults.EMPTY)
                else {
                    searching.value = true
                    emit(search(q))
                    searching.value = false
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SearchResults.EMPTY)

    val uiState: StateFlow<SearchUiState> =
        combine(query, searching, results) { q, isSearching, res ->
            SearchUiState(q, isSearching, res, q.trim().length >= 2)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SearchUiState())

    fun onQueryChange(value: String) { query.value = value }
    fun clear() { query.value = "" }
}
