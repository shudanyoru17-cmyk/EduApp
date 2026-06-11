package com.edu.app.data.mapper

import com.edu.app.data.local.entity.FlashcardEntity
import com.edu.app.data.local.entity.LessonEntity
import com.edu.app.data.local.relation.CategoryWithChapters
import com.edu.app.data.local.relation.ChapterWithLessons
import com.edu.app.data.local.relation.QuestionWithOptions
import com.edu.app.domain.model.*

fun LessonEntity.toDomain() = Lesson(
    id = id, chapterId = chapterId, title = title, summary = summary,
    contentMarkdown = contentMarkdown, estimatedMinutes = estimatedMinutes
)

fun ChapterWithLessons.toDomain() = Chapter(
    id = chapter.id, title = chapter.title,
    lessons = lessons.sortedBy { it.orderIndex }.map { it.toDomain() }
)

fun CategoryWithChapters.toDomain() = Category(
    id = category.id, title = category.title, description = category.description,
    colorHex = category.colorHex,
    chapters = chapters.sortedBy { it.chapter.orderIndex }.map { it.toDomain() }
)

fun QuestionWithOptions.toDomain() = ExamQuestion(
    id = question.id,
    type = question.type,
    prompt = question.prompt,
    explanation = question.explanation,
    difficulty = question.difficulty,
    options = options.sortedBy { it.orderIndex }.map { ExamOption(it.id, it.text, it.isCorrect) },
    acceptedAnswers = question.acceptedAnswers
)

fun FlashcardEntity.toDomain() = Flashcard(
    id = id, subjectId = subjectId, lessonId = lessonId,
    front = frontText, back = backText, hint = hint,
    easeFactor = easeFactor, intervalDays = intervalDays, repetitions = repetitions,
    dueAtMillis = dueAt, lastReviewedAtMillis = lastReviewedAt
)
