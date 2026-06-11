package com.edu.app.feature.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Dest(val route: String, val labelAr: String, val icon: ImageVector) {
    data object Home       : Dest("home", "الرئيسية", Icons.Rounded.Home)
    data object Lessons    : Dest("lessons", "الدروس", Icons.Rounded.MenuBook)
    data object Flashcards : Dest("flashcards", "البطاقات", Icons.Rounded.Style)
    data object Exam       : Dest("exam", "الامتحان", Icons.Rounded.Timer)
    data object Progress   : Dest("progress", "تقدمي", Icons.Rounded.BarChart)
}

val bottomItems = listOf(Dest.Home, Dest.Lessons, Dest.Flashcards, Dest.Exam, Dest.Progress)

object Routes {
    const val SEARCH = "search"
    const val QUIZ = "quiz"
    const val LESSON_DETAIL = "lesson/{lessonId}"
    fun lessonDetail(id: String) = "lesson/$id"
}
