package com.edu.app.data.repository

import com.edu.app.core.common.Clock
import com.edu.app.core.common.IdGenerator
import com.edu.app.data.local.dao.*
import com.edu.app.data.local.entity.ExamAnswerEntity
import com.edu.app.data.local.entity.ExamSessionEntity
import com.edu.app.data.local.entity.FlashcardReviewEntity
import com.edu.app.data.local.fts.FlashcardSearchRow
import com.edu.app.data.mapper.toDomain
import com.edu.app.data.search.FtsQuerySanitizer
import com.edu.app.data.seed.LessonSeeder
import com.edu.app.domain.model.*
import com.edu.app.domain.repository.*
import com.edu.app.domain.srs.ReviewGrade
import com.edu.app.domain.srs.SpacedRepetition
import com.edu.app.domain.srs.SrsState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

// ---------------- Lessons ----------------
class LessonRepositoryImpl(
    private val dao: LessonDao,
    private val seeder: LessonSeeder
) : LessonRepository {
    override fun getCategories(): Flow<List<Category>> =
        dao.observeCategoriesWithChapters().map { list -> list.map { it.toDomain() } }

    override fun getChapters(categoryId: String): Flow<List<Chapter>> =
        dao.observeChapters(categoryId).map { list -> list.map { it.toDomain() } }

    override fun getLesson(lessonId: String): Flow<Lesson?> =
        dao.observeLesson(lessonId).map { it?.toDomain() }

    override suspend fun seedIfEmpty() {
        if (dao.lessonCount() == 0) seeder.seed(dao)
    }
}

// ---------------- Exam ----------------
class ExamRepositoryImpl(
    private val dao: ExamDao,
    private val ids: IdGenerator
) : ExamRepository {
    override suspend fun loadQuestions(config: ExamConfig): List<ExamQuestion> {
        val rows = if (config.categoryId == null)
            dao.randomQuestions(config.questionCount)
        else
            dao.randomQuestionsByCategory(config.categoryId, config.questionCount)
        return rows.map { it.toDomain() }
    }

    override suspend fun saveResult(
        result: ExamResult, config: ExamConfig, startedAt: Long, completedAt: Long
    ) {
        dao.insertSession(
            ExamSessionEntity(
                id = result.sessionId,
                subjectId = config.categoryId ?: "mixed",
                title = "محاكاة امتحان",
                status = ExamStatus.SUBMITTED,
                startedAt = startedAt,
                completedAt = completedAt,
                totalQuestions = result.total,
                correctCount = result.correct,
                scorePercent = result.scorePercent
            )
        )
        dao.insertAnswers(
            result.graded.mapNotNull { g ->
                val ua = g.userAnswer ?: return@mapNotNull null
                ExamAnswerEntity(
                    id = ids.next(),
                    sessionId = result.sessionId,
                    questionId = g.question.id,
                    selectedOptionIds = ua.selectedOptionIds,
                    shortAnswerText = ua.shortAnswerText,
                    isCorrect = g.isCorrect,
                    answeredAt = completedAt
                )
            }
        )
    }
}

// ---------------- Flashcards ----------------
class FlashcardRepositoryImpl(
    private val dao: FlashcardDao,
    private val clock: Clock,
    private val ids: IdGenerator
) : FlashcardRepository {
    override suspend fun buildDailySession(newLimit: Int, dueLimit: Int): DailySession {
        val now = clock.nowMillis()
        return DailySession(
            dueCards = dao.dueCards(now, dueLimit).map { it.toDomain() },
            newCards = dao.newCards(newLimit).map { it.toDomain() }
        )
    }

    override suspend fun submitReview(cardId: String, grade: ReviewGrade): Flashcard {
        val entity = dao.getById(cardId) ?: error("Card not found: $cardId")
        val now = clock.nowMillis()
        val update = SpacedRepetition.review(
            SrsState(entity.easeFactor, entity.intervalDays, entity.repetitions), grade, now
        )
        val updated = entity.copy(
            easeFactor = update.state.easeFactor,
            intervalDays = update.state.intervalDays,
            repetitions = update.state.repetitions,
            dueAt = update.nextDueAtMillis,
            lastReviewedAt = now
        )
        dao.update(updated)
        dao.insertReview(FlashcardReviewEntity(flashcardId = cardId, reviewedAt = now, quality = grade.q))
        return updated.toDomain()
    }

    override fun observeDueCount(): Flow<Int> = dao.observeDueCount(clock.nowMillis())
}

// ---------------- Search ----------------
class SearchRepositoryImpl(
    private val dao: SearchDao,
    private val io: CoroutineDispatcher
) : SearchRepository {
    override suspend fun search(query: String, perTypeLimit: Int): SearchResults {
        val match = FtsQuerySanitizer.build(query) ?: return SearchResults.EMPTY
        return withContext(io) {
            coroutineScope {
                val lessonsD = async { dao.searchLessons(match, perTypeLimit) }
                val questionsD = async { dao.searchQuestions(match, perTypeLimit) }
                val cardsD = async { dao.searchFlashcards(match, perTypeLimit) }
                SearchResults(
                    lessons = lessonsD.await().map {
                        SearchResult.LessonResult(it.id, it.title, it.snippet, it.chapterId)
                    },
                    questions = questionsD.await().map {
                        SearchResult.QuestionResult(it.id, it.prompt, it.snippet, it.lessonId, it.type)
                    },
                    flashcards = cardsD.await().map { row: FlashcardSearchRow ->
                        SearchResult.FlashcardResult(row.id, row.front, row.snippet, row.subjectId)
                    }
                )
            }
        }
    }

    override suspend fun rebuildIndices() = withContext(io) {
        dao.rebuildLessonsIndex()
        dao.rebuildQuestionsIndex()
        dao.rebuildFlashcardsIndex()
    }
}

// ---------------- Progress ----------------
class ProgressRepositoryImpl(
    private val dao: ProgressDao,
    private val clock: Clock
) : ProgressRepository {
    override fun observeStats(): Flow<UserStats> = combine(
        dao.observeCompletedLessons(),
        dao.observeAvgExamScore(),
        dao.observeTotalReviews(),
        dao.observeDueCount(clock.nowMillis())
    ) { completed, avg, reviews, due ->
        UserStats(completed, avg.toInt(), reviews, due)
    }
}
