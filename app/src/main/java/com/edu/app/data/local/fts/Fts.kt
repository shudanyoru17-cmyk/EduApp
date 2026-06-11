package com.edu.app.data.local.fts

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.FtsOptions
import com.edu.app.data.local.entity.FlashcardEntity
import com.edu.app.data.local.entity.LessonEntity
import com.edu.app.data.local.entity.QuestionEntity

@Fts4(
    contentEntity = LessonEntity::class,
    tokenizer = FtsOptions.TOKENIZER_UNICODE61,
    tokenizerArgs = ["remove_diacritics=2"]
)
@Entity(tableName = "lessons_fts")
data class LessonFts(val title: String, val summary: String, val contentMarkdown: String)

@Fts4(
    contentEntity = QuestionEntity::class,
    tokenizer = FtsOptions.TOKENIZER_UNICODE61,
    tokenizerArgs = ["remove_diacritics=2"]
)
@Entity(tableName = "questions_fts")
data class QuestionFts(val prompt: String, val explanation: String?)

@Fts4(
    contentEntity = FlashcardEntity::class,
    tokenizer = FtsOptions.TOKENIZER_UNICODE61,
    tokenizerArgs = ["remove_diacritics=2"]
)
@Entity(tableName = "flashcards_fts")
data class FlashcardFts(val frontText: String, val backText: String, val hint: String?)

// ---- search result rows ----
data class LessonSearchRow(val id: String, val chapterId: String, val title: String, val snippet: String)
data class QuestionSearchRow(val id: String, val lessonId: String?, val type: String, val prompt: String, val snippet: String)
data class FlashcardSearchRow(val id: String, val subjectId: String, val front: String, val snippet: String)
