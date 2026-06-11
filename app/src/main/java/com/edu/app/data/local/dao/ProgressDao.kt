package com.edu.app.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT COUNT(*) FROM user_progress WHERE status = 'COMPLETED'")
    fun observeCompletedLessons(): Flow<Int>

    @Query("SELECT COALESCE(AVG(scorePercent), 0) FROM exam_sessions WHERE status = 'SUBMITTED'")
    fun observeAvgExamScore(): Flow<Double>

    @Query("SELECT COUNT(*) FROM flashcard_reviews")
    fun observeTotalReviews(): Flow<Int>

    @Query("SELECT COUNT(*) FROM flashcards WHERE dueAt <= :now AND lastReviewedAt IS NOT NULL")
    fun observeDueCount(now: Long): Flow<Int>
}
