package com.edu.app.domain.usecase

import com.edu.app.domain.grading.AnswerGrader
import com.edu.app.domain.model.*
import com.edu.app.domain.repository.*
import com.edu.app.domain.srs.ReviewGrade
import kotlinx.coroutines.flow.Flow

// ---- Lessons ----
class GetCategoriesUseCase(private val repo: LessonRepository) {
    operator fun invoke(): Flow<List<Category>> = repo.getCategories()
}
class GetLessonUseCase(private val repo: LessonRepository) {
    operator fun invoke(lessonId: String): Flow<Lesson?> = repo.getLesson(lessonId)
}

// ---- Exam ----
class StartExamUseCase(private val repo: ExamRepository) {
    suspend operator fun invoke(config: ExamConfig): List<ExamQuestion> = repo.loadQuestions(config)
}

class GradeExamUseCase {
    operator fun invoke(
        sessionId: String,
        questions: List<ExamQuestion>,
        answers: Map<String, UserAnswer>,
        config: ExamConfig,
        timeTakenSeconds: Long
    ): ExamResult {
        val graded = questions.map { q ->
            val ua = answers[q.id]
            GradedAnswer(q, ua, AnswerGrader.isCorrect(q, ua))
        }
        val correct = graded.count { it.isCorrect }
        val unanswered = graded.count { it.userAnswer?.isAnswered != true }
        val incorrect = graded.size - correct - unanswered
        val score = if (graded.isEmpty()) 0 else (correct * 100) / graded.size

        val byDiff = Difficulty.entries.associateWith { d ->
            val pool = graded.filter { it.question.difficulty == d }
            DifficultyBreakdown(correct = pool.count { it.isCorrect }, total = pool.size)
        }.filterValues { it.total > 0 }

        return ExamResult(
            sessionId, graded.size, correct, incorrect, unanswered, score,
            passed = score >= config.passThresholdPercent,
            timeTakenSeconds = timeTakenSeconds,
            byDifficulty = byDiff,
            graded = graded
        )
    }
}

// ---- Flashcards ----
class StartDailyRevisionUseCase(private val repo: FlashcardRepository) {
    suspend operator fun invoke(newLimit: Int = 20, dueLimit: Int = 100): DailySession =
        repo.buildDailySession(newLimit, dueLimit)
}
class ReviewCardUseCase(private val repo: FlashcardRepository) {
    suspend operator fun invoke(cardId: String, grade: ReviewGrade): Flashcard =
        repo.submitReview(cardId, grade)
}
class ObserveDueCountUseCase(private val repo: FlashcardRepository) {
    operator fun invoke(): Flow<Int> = repo.observeDueCount()
}

// ---- Search ----
class SearchUseCase(private val repo: SearchRepository) {
    suspend operator fun invoke(query: String): SearchResults = repo.search(query)
}

// ---- Progress ----
class GetUserStatsUseCase(private val repo: ProgressRepository) {
    operator fun invoke(): Flow<UserStats> = repo.observeStats()
}
