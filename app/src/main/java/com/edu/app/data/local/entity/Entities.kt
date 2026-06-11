package com.edu.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.edu.app.domain.model.Difficulty
import com.edu.app.domain.model.ExamStatus
import com.edu.app.domain.model.ProgressStatus
import com.edu.app.domain.model.QuestionType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val colorHex: String,
    val iconRef: String? = null,
    val orderIndex: Int
)

@Entity(
    tableName = "chapters",
    foreignKeys = [ForeignKey(CategoryEntity::class, ["id"], ["categoryId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("categoryId")]
)
data class ChapterEntity(
    @PrimaryKey val id: String,
    val categoryId: String,
    val title: String,
    val orderIndex: Int
)

@Entity(
    tableName = "lessons",
    foreignKeys = [ForeignKey(ChapterEntity::class, ["id"], ["chapterId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("chapterId")]
)
data class LessonEntity(
    @PrimaryKey val id: String,
    val chapterId: String,
    val title: String,
    val summary: String,
    val contentMarkdown: String,
    val estimatedMinutes: Int,
    val orderIndex: Int,
    val isDownloaded: Boolean = true
)

@Entity(
    tableName = "questions",
    foreignKeys = [ForeignKey(LessonEntity::class, ["id"], ["lessonId"], onDelete = ForeignKey.SET_NULL)],
    indices = [Index("lessonId"), Index("type")]
)
data class QuestionEntity(
    @PrimaryKey val id: String,
    val lessonId: String? = null,
    val type: QuestionType,
    val prompt: String,
    val explanation: String? = null,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val acceptedAnswers: List<String> = emptyList(),
    val correctBoolean: Boolean? = null
)

@Entity(
    tableName = "answer_options",
    foreignKeys = [ForeignKey(QuestionEntity::class, ["id"], ["questionId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("questionId")]
)
data class AnswerOptionEntity(
    @PrimaryKey val id: String,
    val questionId: String,
    val text: String,
    val isCorrect: Boolean,
    val orderIndex: Int
)

@Entity(
    tableName = "user_progress",
    foreignKeys = [ForeignKey(LessonEntity::class, ["id"], ["lessonId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("lessonId")]
)
data class UserProgressEntity(
    @PrimaryKey val lessonId: String,
    val status: ProgressStatus = ProgressStatus.NOT_STARTED,
    val progressPercent: Int = 0,
    val lastAccessedAt: Long = 0L,
    val timeSpentSeconds: Long = 0L
)

@Entity(
    tableName = "question_mastery",
    foreignKeys = [ForeignKey(QuestionEntity::class, ["id"], ["questionId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("questionId")]
)
data class QuestionMasteryEntity(
    @PrimaryKey val questionId: String,
    val timesSeen: Int = 0,
    val timesCorrect: Int = 0,
    val lastAnsweredAt: Long = 0L
)

@Entity(tableName = "exam_sessions", indices = [Index("subjectId")])
data class ExamSessionEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val title: String,
    val status: ExamStatus = ExamStatus.IN_PROGRESS,
    val startedAt: Long,
    val completedAt: Long? = null,
    val totalQuestions: Int,
    val correctCount: Int = 0,
    val scorePercent: Int = 0,
    val durationLimitSeconds: Long? = null
)

@Entity(
    tableName = "exam_answers",
    foreignKeys = [ForeignKey(ExamSessionEntity::class, ["id"], ["sessionId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("sessionId"), Index("questionId")]
)
data class ExamAnswerEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val questionId: String,
    val selectedOptionIds: List<String> = emptyList(),
    val shortAnswerText: String? = null,
    val isCorrect: Boolean,
    val answeredAt: Long
)

@Entity(
    tableName = "flashcards",
    foreignKeys = [ForeignKey(LessonEntity::class, ["id"], ["lessonId"], onDelete = ForeignKey.SET_NULL)],
    indices = [Index("subjectId"), Index("lessonId"), Index("dueAt")]
)
data class FlashcardEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val lessonId: String? = null,
    val frontText: String,
    val backText: String,
    val hint: String? = null,
    val easeFactor: Float = 2.5f,
    val intervalDays: Int = 0,
    val repetitions: Int = 0,
    val dueAt: Long = 0L,
    val lastReviewedAt: Long? = null
)

@Entity(
    tableName = "flashcard_reviews",
    foreignKeys = [ForeignKey(FlashcardEntity::class, ["id"], ["flashcardId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("flashcardId")]
)
data class FlashcardReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val flashcardId: String,
    val reviewedAt: Long,
    val quality: Int
)
