package com.edu.app.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.edu.app.data.local.entity.*

data class ChapterWithLessons(
    @Embedded val chapter: ChapterEntity,
    @Relation(parentColumn = "id", entityColumn = "chapterId")
    val lessons: List<LessonEntity>
)

data class CategoryWithChapters(
    @Embedded val category: CategoryEntity,
    @Relation(entity = ChapterEntity::class, parentColumn = "id", entityColumn = "categoryId")
    val chapters: List<ChapterWithLessons>
)

data class QuestionWithOptions(
    @Embedded val question: QuestionEntity,
    @Relation(parentColumn = "id", entityColumn = "questionId")
    val options: List<AnswerOptionEntity>
)
