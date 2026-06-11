package com.edu.app.domain.grading

import com.edu.app.domain.model.ExamQuestion
import com.edu.app.domain.model.QuestionType
import com.edu.app.domain.model.UserAnswer

object AnswerGrader {

    fun isCorrect(question: ExamQuestion, answer: UserAnswer?): Boolean {
        if (answer == null || !answer.isAnswered) return false
        return when (question.type) {
            QuestionType.MCQ, QuestionType.TRUE_FALSE -> gradeChoice(question, answer)
            QuestionType.SHORT_ANSWER -> gradeShort(question, answer)
        }
    }

    private fun gradeChoice(q: ExamQuestion, a: UserAnswer): Boolean {
        val correctIds = q.options.filter { it.isCorrect }.map { it.id }.toSet()
        return correctIds.isNotEmpty() && a.selectedOptionIds.toSet() == correctIds
    }

    private fun gradeShort(q: ExamQuestion, a: UserAnswer): Boolean {
        val given = normalizeArabic(a.shortAnswerText.orEmpty())
        return q.acceptedAnswers.any { normalizeArabic(it) == given }
    }

    fun normalizeArabic(input: String): String {
        val tashkeel = Regex("[\\u064B-\\u0652\\u0670]")
        return input.trim()
            .replace(tashkeel, "")
            .replace('أ', 'ا').replace('إ', 'ا').replace('آ', 'ا')
            .replace('ى', 'ي')
            .replace('ة', 'ه')
            .replace(Regex("\\s+"), " ")
            .lowercase()
    }
}
