package com.edu.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.edu.app.data.local.dao.*
import com.edu.app.data.local.entity.*
import com.edu.app.data.local.fts.FlashcardFts
import com.edu.app.data.local.fts.LessonFts
import com.edu.app.data.local.fts.QuestionFts

@Database(
    entities = [
        CategoryEntity::class, ChapterEntity::class, LessonEntity::class,
        QuestionEntity::class, AnswerOptionEntity::class,
        UserProgressEntity::class, QuestionMasteryEntity::class,
        ExamSessionEntity::class, ExamAnswerEntity::class,
        FlashcardEntity::class, FlashcardReviewEntity::class,
        LessonFts::class, QuestionFts::class, FlashcardFts::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lessonDao(): LessonDao
    abstract fun examDao(): ExamDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun searchDao(): SearchDao
    abstract fun progressDao(): ProgressDao
}
