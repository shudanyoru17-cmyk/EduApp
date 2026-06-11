package com.edu.app.data.seed

import com.edu.app.data.local.dao.LessonDao
import com.edu.app.data.local.entity.*
import com.edu.app.domain.model.Difficulty
import com.edu.app.domain.model.QuestionType

/** Seeds offline sample content so the app is usable on first launch. */
class LessonSeeder {
    suspend fun seed(dao: LessonDao) {
        dao.upsertCategories(listOf(
            CategoryEntity("cat_math", "الرياضيات", "أساسيات ومسائل", "#1E6F5C", orderIndex = 0),
            CategoryEntity("cat_science", "العلوم", "الفيزياء والكيمياء", "#2D6A9F", orderIndex = 1)
        ))
        dao.upsertChapters(listOf(
            ChapterEntity("ch_alg", "cat_math", "الجبر", 0),
            ChapterEntity("ch_geo", "cat_math", "الهندسة", 1),
            ChapterEntity("ch_phys", "cat_science", "الحركة", 0)
        ))
        dao.upsertLessons(listOf(
            LessonEntity(
                "ls_1", "ch_alg", "المعادلات من الدرجة الأولى",
                "تعلّم حل المعادلات البسيطة.",
                "# المعادلات من الدرجة الأولى\n\nالمعادلة على الصورة **ax + b = 0**.\n\n## خطوات الحل\n- انقل الحد الثابت\n- اقسم على معامل المتغير\n\n> مثال: ٢س + ٤ = ٠  ⟶  س = -٢",
                8, 0
            ),
            LessonEntity(
                "ls_2", "ch_alg", "المتباينات",
                "مقارنة القيم وحل المتباينات.",
                "# المتباينات\n\nالمتباينة تستخدم الرموز **>** و **<**.",
                6, 1
            ),
            LessonEntity(
                "ls_3", "ch_phys", "السرعة والتسارع",
                "مفاهيم الحركة الأساسية.",
                "# الحركة\n\n**السرعة** = المسافة ÷ الزمن.\n\n- السرعة المتجهة لها اتجاه\n- التسارع هو تغيّر السرعة",
                10, 0
            )
        ))
        dao.upsertQuestions(listOf(
            QuestionEntity("q1", "ls_1", QuestionType.MCQ, "ما حل المعادلة ٢س + ٤ = ٠؟",
                "نطرح ٤ ثم نقسم على ٢.", Difficulty.EASY),
            QuestionEntity("q2", "ls_1", QuestionType.TRUE_FALSE, "المعادلة الخطية لها حل واحد دائماً.",
                "صحيح للمعادلات من الدرجة الأولى.", Difficulty.MEDIUM, correctBoolean = true),
            QuestionEntity("q3", "ls_3", QuestionType.SHORT_ANSWER, "ما وحدة قياس السرعة؟",
                "متر لكل ثانية.", Difficulty.EASY, acceptedAnswers = listOf("متر/ثانية", "م/ث", "متر لكل ثانية"))
        ))
        dao.upsertOptions(listOf(
            AnswerOptionEntity("o1", "q1", "س = -٢", true, 0),
            AnswerOptionEntity("o2", "q1", "س = ٢", false, 1),
            AnswerOptionEntity("o3", "q1", "س = ٤", false, 2),
            AnswerOptionEntity("o4", "q1", "س = ٠", false, 3),
            AnswerOptionEntity("o5", "q2", "صحيح", true, 0),
            AnswerOptionEntity("o6", "q2", "خطأ", false, 1)
        ))
        dao.upsertFlashcards(listOf(
            FlashcardEntity("fc1", "cat_math", "ls_1", "ما صيغة المعادلة الخطية؟", "ax + b = 0", "حرفان فقط"),
            FlashcardEntity("fc2", "cat_science", "ls_3", "ما هي السرعة؟", "المسافة مقسومة على الزمن", null),
            FlashcardEntity("fc3", "cat_math", "ls_2", "رمز أكبر من؟", ">", null)
        ))
    }
}
