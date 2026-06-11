package com.edu.app.feature.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.edu.app.core.ui.AppTheme
import com.edu.app.feature.exam.ExamRoute
import com.edu.app.feature.flashcards.RevisionScreen
import com.edu.app.feature.home.HomeScreen
import com.edu.app.feature.lessons.LessonDetailScreen
import com.edu.app.feature.lessons.LessonsScreen
import com.edu.app.feature.progress.ProgressScreen
import com.edu.app.feature.quiz.QuizScreen
import com.edu.app.feature.search.SearchScreen

@Composable
fun AppRoot(appViewModel: AppViewModel) {
    AppTheme {
        val nav = rememberNavController()
        val backStack by nav.currentBackStackEntryAsState()
        val current = backStack?.destination?.route
        val showBottomBar = bottomItems.any { it.route == current }

        Scaffold(
            bottomBar = { if (showBottomBar) BottomBar(nav, current) }
        ) { padding ->
            AppNavGraph(nav, Modifier.padding(padding))
        }
    }
}

@Composable
private fun BottomBar(nav: NavHostController, current: String?) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        bottomItems.forEach { dest ->
            NavigationBarItem(
                selected = current == dest.route,
                onClick = {
                    nav.navigate(dest.route) {
                        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(dest.icon, dest.labelAr) },
                label = { Text(dest.labelAr, style = MaterialTheme.typography.labelSmall) }
            )
        }
    }
}

@Composable
private fun AppNavGraph(nav: NavHostController, modifier: Modifier = Modifier) {
    NavHost(nav, startDestination = Dest.Home.route, modifier = modifier) {

        composable(Dest.Home.route) {
            HomeScreen(
                onOpenLessons = { nav.navigate(Dest.Lessons.route) },
                onOpenExam = { nav.navigate(Dest.Exam.route) },
                onOpenSearch = { nav.navigate(Routes.SEARCH) }
            )
        }

        composable(Dest.Lessons.route) {
            LessonsScreen(onLessonClick = { id -> nav.navigate(Routes.lessonDetail(id)) })
        }

        composable(
            route = Routes.LESSON_DETAIL,
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) {
            LessonDetailScreen(
                onBack = { nav.popBackStack() },
                onStartQuiz = { nav.navigate(Routes.QUIZ) }
            )
        }

        composable(Routes.QUIZ) { QuizScreen(onFinish = { nav.popBackStack() }) }

        composable(Dest.Flashcards.route) { RevisionScreen(onExit = { nav.popBackStack() }) }

        composable(Dest.Exam.route) {
            ExamRoute(onExitToHome = {
                nav.navigate(Dest.Home.route) { popUpTo(Dest.Home.route) { inclusive = true } }
            })
        }

        composable(Dest.Progress.route) {
            ProgressScreen(onOpenSearch = { nav.navigate(Routes.SEARCH) })
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                onOpenLesson = { id -> nav.navigate(Routes.lessonDetail(id)) },
                onOpenQuestion = { },
                onOpenFlashcard = { nav.navigate(Dest.Flashcards.route) }
            )
        }
    }
}
