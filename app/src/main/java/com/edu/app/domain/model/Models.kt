package com.edu.app.domain.model

// ---------------- Lessons ----------------
data class Category(
    val id: String,
    val title: String,
    val description: String,
    val colorHex: String,
    val chapters: List<Chapter>
)

data class Chapter(
    val id: String,
    val title: String,
    val lessons: List<Lesson>
)

data class Lesson(
    val id: String,
    val chapterId: String,
    val title: String,
    val summary: String,
    val contentMarkdown: String,
    val estimatedMinutes: Int
)

// ---------------- Exam ----------------
data class ExamConfig(
    val categoryId: String?,
    val questionCount: Int,
    val durationMinutes: Int,
    val passThresholdPercent: Int = 60
)

data class ExamOption(val id: String, val text: String, val isCorrect: Boolean)

data class ExamQuestion(
    val id: String,
    val type: QuestionType,
    val prompt: String,
    val explanation: String?,
    val difficulty: Difficulty,
    val options: List<ExamOption> = emptyList(),
    val acceptedAnswers: List<String> = emptyList()
)

data class UserAnswer(
    val questionId: String,
    val selectedOptionIds: List<String> = emptyList(),
    val shortAnswerText: String? = null
) {
    val isAnswered: Boolean
        get() = selectedOptionIds.isNotEmpty() || !shortAnswerText.isNullOrBlank()
}

data class GradedAnswer(
    val question: ExamQuestion,
    val userAnswer: UserAnswer?,
    val isCorrect: Boolean
)

data class DifficultyBreakdown(val correct: Int, val total: Int)

data class ExamResult(
    val sessionId: String,
    val total: Int,
    val correct: Int,
    val incorrect: Int,
    val unanswered: Int,
    val scorePercent: Int,
    val passed: Boolean,
    val timeTakenSeconds: Long,
    val byDifficulty: Map<Difficulty, DifficultyBreakdown>,
    val graded: List<GradedAnswer>
)

// ---------------- Flashcards ----------------
data class Flashcard(
    val id: String,
    val subjectId: String,
    val lessonId: String?,
    val front: String,
    val back: String,
    val hint: String?,
    val easeFactor: Float,
    val intervalDays: Int,
    val repetitions: Int,
    val dueAtMillis: Long,
    val lastReviewedAtMillis: Long?
)

data class DailySession(
    val dueCards: List<Flashcard>,
    val newCards: List<Flashcard>
) {
    val queue: List<Flashcard> = dueCards + newCards
    val total: Int get() = queue.size
}

data class SessionSummary(
    val reviewed: Int, val again: Int, val good: Int, val easy: Int, val hard: Int
)

// ---------------- Search ----------------
enum class SearchType { LESSON, QUESTION, FLASHCARD }

sealed interface SearchResult {
    val id: String; val title: String; val snippet: String; val type: SearchType

    data class LessonResult(
        override val id: String, override val title: String, override val snippet: String,
        val chapterId: String
    ) : SearchResult { override val type = SearchType.LESSON }

    data class QuestionResult(
        override val id: String, override val title: String, override val snippet: String,
        val lessonId: String?, val questionType: String
    ) : SearchResult { override val type = SearchType.QUESTION }

    data class FlashcardResult(
        override val id: String, override val title: String, override val snippet: String,
        val subjectId: String
    ) : SearchResult { override val type = SearchType.FLASHCARD }
}

data class SearchResults(
    val lessons: List<SearchResult.LessonResult>,
    val questions: List<SearchResult.QuestionResult>,
    val flashcards: List<SearchResult.FlashcardResult>
) {
    val isEmpty get() = lessons.isEmpty() && questions.isEmpty() && flashcards.isEmpty()
    val total get() = lessons.size + questions.size + flashcards.size
    companion object { val EMPTY = SearchResults(emptyList(), emptyList(), emptyList()) }
}

// ---------------- Progress ----------------
data class UserStats(
    val completedLessons: Int,
    val averageExamScore: Int,
    val totalReviews: Int,
    val dueFlashcards: Int
)
