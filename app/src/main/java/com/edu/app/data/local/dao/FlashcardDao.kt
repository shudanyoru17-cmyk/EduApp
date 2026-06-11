package com.edu.app.data.local.dao

import androidx.room.*
import com.edu.app.data.local.entity.FlashcardEntity
import com.edu.app.data.local.entity.FlashcardReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Query("""
        SELECT * FROM flashcards
        WHERE dueAt <= :now AND lastReviewedAt IS NOT NULL
        ORDER BY dueAt ASC LIMIT :limit
    """)
    suspend fun dueCards(now: Long, limit: Int): List<FlashcardEntity>

    @Query("SELECT * FROM flashcards WHERE lastReviewedAt IS NULL ORDER BY id ASC LIMIT :limit")
    suspend fun newCards(limit: Int): List<FlashcardEntity>

    @Query("SELECT COUNT(*) FROM flashcards WHERE dueAt <= :now AND lastReviewedAt IS NOT NULL")
    fun observeDueCount(now: Long): Flow<Int>

    @Query("SELECT * FROM flashcards WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): FlashcardEntity?

    @Update suspend fun update(card: FlashcardEntity)
    @Insert suspend fun insertReview(review: FlashcardReviewEntity)
}
