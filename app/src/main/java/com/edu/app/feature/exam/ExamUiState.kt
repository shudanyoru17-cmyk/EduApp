package com.edu.app.feature.exam

import com.edu.app.domain.model.ExamQuestion
import com.edu.app.domain.model.ExamResult
import com.edu.app.domain.model.UserAnswer

sealed interface ExamUiState {
    data object Configuring : ExamUiState
    data object Loading : ExamUiState
    data class Running(
        val questions: List<ExamQuestion>,
        val index: Int,
        val answers: Map<String, UserAnswer>,
        val remainingSeconds: Long,
        val totalSeconds: Long
    ) : ExamUiState {
        val current: ExamQuestion get() = questions[index]
        val isLast: Boolean get() = index == questions.lastIndex
    }
    data class Finished(val result: ExamResult) : ExamUiState
    data class Error(val message: String) : ExamUiState
}
