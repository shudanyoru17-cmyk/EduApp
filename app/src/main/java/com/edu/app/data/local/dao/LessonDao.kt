package com.edu.app.data.local.dao

import androidx.room.*
import com.edu.app.data.local.entity.*
import com.edu.app.data.local.relation.CategoryWithChapters
import com.edu.app.data.local.relation.ChapterWithLessons
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {
    @Transaction
    @Query("SELECT * FROM categories ORDER BY orderIndex ASC")
    fun observeCategoriesWithChapters(): Flow<List<CategoryWithChapters>>

    @Transaction
    @Query("SELECT * FROM chapters WHERE categoryId = :categoryId ORDER BY orderIndex ASC")
    fun observeChapters(categoryId: String): Flow<List<ChapterWithLessons>>

    @Query("SELECT * FROM lessons WHERE id = :lessonId LIMIT 1")
    fun observeLesson(lessonId: String): Flow<LessonEntity?>

    @Upsert suspend fun upsertCategories(items: List<CategoryEntity>)
    @Upsert suspend fun upsertChapters(items: List<ChapterEntity>)
    @Upsert suspend fun upsertLessons(items: List<LessonEntity>)
    @Upsert suspend fun upsertQuestions(items: List<QuestionEntity>)
    @Upsert suspend fun upsertOptions(items: List<AnswerOptionEntity>)
    @Upsert suspend fun upsertFlashcards(items: List<FlashcardEntity>)

    @Query("SELECT COUNT(*) FROM lessons") suspend fun lessonCount(): Int
}
