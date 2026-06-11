package com.edu.app.feature.lessons

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.app.core.ui.SectionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
    onBack: () -> Unit,
    onStartQuiz: () -> Unit,
    viewModel: LessonDetailViewModel = hiltViewModel()
) {
    val lesson by viewModel.lesson.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(lesson?.title ?: "الدرس", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "رجوع") }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onStartQuiz,
                icon = { Icon(Icons.Rounded.Quiz, null) },
                text = { Text("اختبار") }
            )
        }
    ) { padding ->
        val current = lesson
        if (current == null) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) { CircularProgressIndicator() }
        } else {
            Column(
                Modifier.fillMaxSize().padding(padding).padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(current.summary, style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                ArabicMarkdownText(current.contentMarkdown)
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}
