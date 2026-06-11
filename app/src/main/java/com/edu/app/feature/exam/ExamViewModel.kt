package com.edu.app.feature.exam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.app.core.common.Clock
import com.edu.app.core.common.IdGenerator
import com.edu.app.domain.model.ExamConfig
import com.edu.app.domain.model.UserAnswer
import com.edu.app.domain.repository.ExamRepository
import com.edu.app.domain.usecase.GradeExamUseCase
import com.edu.app.domain.usecase.StartExamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val startExam: StartExamUseCase,
    private val gradeExam: GradeExamUseCase,
    private val repository: ExamRepository,
    private val ids: IdGenerator,
    private val clock: Clock
) : ViewModel() {

    private val _state = MutableStateFlow<ExamUiState>(ExamUiState.Configuring)
    val state: StateFlow<ExamUiState> = _state.asStateFlow()

    private var timerJob: Job? = null
    private var config: ExamConfig? = null
    private var startedAt: Long = 0L

    fun begin(cfg: ExamConfig) {
        config = cfg
        _state.value = ExamUiState.Loading
        viewModelScope.launch {
            val questions = runCatching { startExam(cfg) }.getOrElse {
                _state.value = ExamUiState.Error("تعذّر تحميل الأسئلة"); return@launch
            }
            if (questions.isEmpty()) {
                _state.value = ExamUiState.Error("لا توجد أسئلة كافية"); return@launch
            }
            startedAt = clock.nowMillis()
            val total = cfg.durationMinutes * 60L
            _state.value = ExamUiState.Running(questions, 0, emptyMap(), total, total)
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1_000)
                val s = _state.value as? ExamUiState.Running ?: break
                val remaining = s.remainingSeconds - 1
                if (remaining <= 0) { _state.value = s.copy(remainingSeconds = 0); finish(true); break }
                _state.value = s.copy(remainingSeconds = remaining)
            }
        }
    }

    fun selectOption(questionId: String, optionId: String) {
        val s = _state.value as? ExamUiState.Running ?: return
        val prev = s.answers[questionId] ?: UserAnswer(questionId)
        updateAnswer(s, prev.copy(selectedOptionIds = listOf(optionId)))
    }

    fun setShortAnswer(questionId: String, text: String) {
        val s = _state.value as? ExamUiState.Running ?: return
        val prev = s.answers[questionId] ?: UserAnswer(questionId)
        updateAnswer(s, prev.copy(shortAnswerText = text))
    }

    private fun updateAnswer(s: ExamUiState.Running, answer: UserAnswer) {
        _state.value = s.copy(answers = s.answers + (answer.questionId to answer))
    }

    fun next() = moveTo { (it + 1).coerceAtMost(questions().lastIndex) }
    fun previous() = moveTo { (it - 1).coerceAtLeast(0) }

    private fun moveTo(transform: (Int) -> Int) {
        val s = _state.value as? ExamUiState.Running ?: return
        _state.value = s.copy(index = transform(s.index))
    }

    private fun questions() = (_state.value as? ExamUiState.Running)?.questions ?: emptyList()

    fun finish(autoSubmitted: Boolean = false) {
        val s = _state.value as? ExamUiState.Running ?: return
        val cfg = config ?: return
        timerJob?.cancel()
        val completedAt = clock.nowMillis()
        val timeTaken = ((completedAt - startedAt) / 1000).coerceAtLeast(0)
        val result = gradeExam(ids.next(), s.questions, s.answers, cfg, timeTaken)
        _state.value = ExamUiState.Finished(result)
        viewModelScope.launch {
            runCatching { repository.saveResult(result, cfg, startedAt, completedAt) }
        }
    }

    override fun onCleared() { timerJob?.cancel(); super.onCleared() }
}
