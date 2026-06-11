package com.edu.app.domain.repository

import com.edu.app.domain.model.*
import com.edu.app.domain.srs.ReviewGrade
import kotlinx.coroutines.flow.Flow

interface LessonRepository {
    fun getCategories(): Flow<List<Category>>
    fun getChapters(categoryId: String): Flow<List<Chapter>>
    fun getLesson(lessonId: String): Flow<Lesson?>
    suspend fun seedIfEmpty()
}

interface ExamRepository {
    suspend fun loadQuestions(config: ExamConfig): List<ExamQuestion>
    suspend fun saveResult(result: ExamResult, config: ExamConfig, startedAt: Long, completedAt: Long)
}

interface FlashcardRepository {
    suspend fun buildDailySession(newLimit: Int, dueLimit: Int): DailySession
    suspend fun submitReview(cardId: String, grade: ReviewGrade): Flashcard
    fun observeDueCount(): Flow<Int>
}

interface SearchRepository {
    suspend fun search(query: String, perTypeLimit: Int = 20): SearchResults
    suspend fun rebuildIndices()
}

interface ProgressRepository {
    fun observeStats(): Flow<UserStats>
}
