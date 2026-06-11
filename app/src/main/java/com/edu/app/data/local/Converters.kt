package com.edu.app.data.local

import androidx.room.TypeConverter
import com.edu.app.domain.model.Difficulty
import com.edu.app.domain.model.ExamStatus
import com.edu.app.domain.model.ProgressStatus
import com.edu.app.domain.model.QuestionType

class Converters {
    @TypeConverter fun fromStringList(v: List<String>?): String? = v?.joinToString("‖")
    @TypeConverter fun toStringList(v: String?): List<String> =
        if (v.isNullOrEmpty()) emptyList() else v.split("‖")

    @TypeConverter fun fromQuestionType(t: QuestionType) = t.name
    @TypeConverter fun toQuestionType(s: String) = QuestionType.valueOf(s)
    @TypeConverter fun fromDifficulty(t: Difficulty) = t.name
    @TypeConverter fun toDifficulty(s: String) = Difficulty.valueOf(s)
    @TypeConverter fun fromProgress(t: ProgressStatus) = t.name
    @TypeConverter fun toProgress(s: String) = ProgressStatus.valueOf(s)
    @TypeConverter fun fromExamStatus(t: ExamStatus) = t.name
    @TypeConverter fun toExamStatus(s: String) = ExamStatus.valueOf(s)
}
