package com.edu.app.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.edu.app.data.local.fts.FlashcardSearchRow
import com.edu.app.data.local.fts.LessonSearchRow
import com.edu.app.data.local.fts.QuestionSearchRow

@Dao
interface SearchDao {
    @Query("""
        SELECT l.id AS id, l.chapterId AS chapterId, l.title AS title,
               snippet(lessons_fts, '[', ']', '…', -1, 12) AS snippet
        FROM lessons_fts JOIN lessons l ON l.rowid = lessons_fts.rowid
        WHERE lessons_fts MATCH :match LIMIT :limit
    """)
    suspend fun searchLessons(match: String, limit: Int): List<LessonSearchRow>

    @Query("""
        SELECT q.id AS id, q.lessonId AS lessonId, q.type AS type, q.prompt AS prompt,
               snippet(questions_fts, '[', ']', '…', -1, 12) AS snippet
        FROM questions_fts JOIN questions q ON q.rowid = questions_fts.rowid
        WHERE questions_fts MATCH :match LIMIT :limit
    """)
    suspend fun searchQuestions(match: String, limit: Int): List<QuestionSearchRow>

    @Query("""
        SELECT f.id AS id, f.subjectId AS subjectId, f.frontText AS front,
               snippet(flashcards_fts, '[', ']', '…', -1, 12) AS snippet
        FROM flashcards_fts JOIN flashcards f ON f.rowid = flashcards_fts.rowid
        WHERE flashcards_fts MATCH :match LIMIT :limit
    """)
    suspend fun searchFlashcards(match: String, limit: Int): List<FlashcardSearchRow>

    @Query("INSERT INTO lessons_fts(lessons_fts) VALUES('rebuild')")
    suspend fun rebuildLessonsIndex()
    @Query("INSERT INTO questions_fts(questions_fts) VALUES('rebuild')")
    suspend fun rebuildQuestionsIndex()
    @Query("INSERT INTO flashcards_fts(flashcards_fts) VALUES('rebuild')")
    suspend fun rebuildFlashcardsIndex()
}
