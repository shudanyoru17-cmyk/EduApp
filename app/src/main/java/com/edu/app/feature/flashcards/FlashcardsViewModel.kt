package com.edu.app.feature.flashcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.app.domain.model.Flashcard
import com.edu.app.domain.model.SessionSummary
import com.edu.app.domain.srs.ReviewGrade
import com.edu.app.domain.usecase.ReviewCardUseCase
import com.edu.app.domain.usecase.StartDailyRevisionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface RevisionUiState {
    data object Loading : RevisionUiState
    data object Empty : RevisionUiState
    data class Reviewing(
        val queue: List<Flashcard>,
        val index: Int,
        val isRevealed: Boolean,
        val summary: SessionSummary
    ) : RevisionUiState {
        val current: Flashcard get() = queue[index]
        val position: Int get() = index + 1
        val total: Int get() = queue.size
    }
    data class Done(val summary: SessionSummary) : RevisionUiState
}

@HiltViewModel
class FlashcardsViewModel @Inject constructor(
    private val startRevision: StartDailyRevisionUseCase,
    private val reviewCard: ReviewCardUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<RevisionUiState>(RevisionUiState.Loading)
    val state: StateFlow<RevisionUiState> = _state.asStateFlow()

    fun loadDailySession() {
        _state.value = RevisionUiState.Loading
        viewModelScope.launch {
            val session = startRevision()
            _state.value = if (session.total == 0) RevisionUiState.Empty
            else RevisionUiState.Reviewing(session.queue, 0, false, SessionSummary(0, 0, 0, 0, 0))
        }
    }

    fun reveal() {
        val s = _state.value as? RevisionUiState.Reviewing ?: return
        _state.value = s.copy(isRevealed = true)
    }

    fun grade(grade: ReviewGrade) {
        val s = _state.value as? RevisionUiState.Reviewing ?: return
        val card = s.current
        viewModelScope.launch {
            runCatching { reviewCard(card.id, grade) }
            val tallied = tally(s.summary, grade)
            _state.value = if (s.index == s.queue.lastIndex) RevisionUiState.Done(tallied)
            else s.copy(index = s.index + 1, isRevealed = false, summary = tallied)
        }
    }

    private fun tally(sum: SessionSummary, grade: ReviewGrade) = sum.copy(
        reviewed = sum.reviewed + 1,
        again = sum.again + if (grade == ReviewGrade.AGAIN) 1 else 0,
        hard = sum.hard + if (grade == ReviewGrade.HARD) 1 else 0,
        good = sum.good + if (grade == ReviewGrade.GOOD) 1 else 0,
        easy = sum.easy + if (grade == ReviewGrade.EASY) 1 else 0
    )
}
