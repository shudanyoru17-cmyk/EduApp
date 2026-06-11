package com.edu.app.feature.lessons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.app.domain.model.Category
import com.edu.app.domain.model.Lesson
import com.edu.app.domain.repository.LessonRepository
import com.edu.app.domain.usecase.GetCategoriesUseCase
import com.edu.app.domain.usecase.GetLessonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LessonsListUiState(
    val isLoading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val expandedCategoryIds: Set<String> = emptySet()
)

@HiltViewModel
class LessonsListViewModel @Inject constructor(
    getCategories: GetCategoriesUseCase,
    private val repository: LessonRepository
) : ViewModel() {

    private val expanded = MutableStateFlow<Set<String>>(emptySet())

    val uiState: StateFlow<LessonsListUiState> =
        combine(getCategories(), expanded) { categories, expandedIds ->
            LessonsListUiState(false, categories, expandedIds)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LessonsListUiState())

    init { viewModelScope.launch { repository.seedIfEmpty() } }

    fun toggleCategory(id: String) {
        expanded.update { if (id in it) it - id else it + id }
    }
}

@HiltViewModel
class LessonDetailViewModel @Inject constructor(
    getLesson: GetLessonUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"])
    val lesson: StateFlow<Lesson?> = getLesson(lessonId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
