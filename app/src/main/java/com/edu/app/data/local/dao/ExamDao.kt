package com.edu.app.data.local.dao

import androidx.room.*
import com.edu.app.data.local.entity.ExamAnswerEntity
import com.edu.app.data.local.entity.ExamSessionEntity
import com.edu.app.data.local.relation.QuestionWithOptions

@Dao
interface ExamDao {
    @Transaction
    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT :count")
    suspend fun randomQuestions(count: Int): List<QuestionWithOptions>

    @Transaction
    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN lessons l   ON q.lessonId = l.id
        INNER JOIN chapters ch ON l.chapterId = ch.id
        WHERE ch.categoryId = :categoryId
        ORDER BY RANDOM() LIMIT :count
    """)
    suspend fun randomQuestionsByCategory(categoryId: String, count: Int): List<QuestionWithOptions>

    @Insert suspend fun insertSession(session: ExamSessionEntity)
    @Insert suspend fun insertAnswers(answers: List<ExamAnswerEntity>)
}
