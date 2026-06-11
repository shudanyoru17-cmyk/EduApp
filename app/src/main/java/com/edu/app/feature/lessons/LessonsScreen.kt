package com.edu.app.feature.lessons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toColorInt
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.edu.app.core.ui.AppTopBar
import com.edu.app.core.ui.SectionCard
import com.edu.app.domain.model.Category
import com.edu.app.domain.model.Lesson

@Composable
fun LessonsScreen(
    onLessonClick: (String) -> Unit,
    viewModel: LessonsListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(topBar = { AppTopBar("الدروس") }) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) { CircularProgressIndicator() }
            return@Scaffold
        }
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(state.categories, key = { it.id }) { category ->
                CategoryCard(category, category.id in state.expandedCategoryIds,
                    { viewModel.toggleCategory(category.id) }, onLessonClick)
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: Category, expanded: Boolean, onToggle: () -> Unit, onLessonClick: (String) -> Unit
) {
    val accent = runCatching { Color(category.colorHex.toColorInt()) }
        .getOrDefault(MaterialTheme.colorScheme.primary)
    SectionCard {
        Row(Modifier.fillMaxWidth().clickable(onClick = onToggle),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(category.title, style = MaterialTheme.typography.titleMedium, color = accent)
                Text(category.description, style = MaterialTheme.typography.bodyLarge)
            }
            Icon(if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore, null)
        }
        AnimatedVisibility(visible = expanded) {
            Column(Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                category.chapters.forEach { chapter ->
                    Text(chapter.title, style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right)
                    chapter.lessons.forEach { lesson ->
                        LessonRow(lesson) { onLessonClick(lesson.id) }
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonRow(lesson: Lesson, onClick: () -> Unit) {
    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(lesson.title, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Right)
                Text("${lesson.estimatedMinutes} دقائق", style = MaterialTheme.typography.labelSmall)
            }
            Icon(Icons.AutoMirrored.Rounded.ArrowBackIos, null)
        }
    }
}
