package com.edu.app.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Style
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.app.core.ui.SectionCard
import com.edu.app.domain.model.SearchResults

@Composable
fun SearchScreen(
    onOpenLesson: (String) -> Unit,
    onOpenQuestion: (String) -> Unit,
    onOpenFlashcard: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            SearchField(state.query, viewModel::onQueryChange, viewModel::clear)
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isSearching -> LinearProgressIndicator(Modifier.fillMaxWidth())
                !state.hasSearched -> Hint("اكتب كلمتين على الأقل للبحث")
                state.results.isEmpty -> Hint("لا توجد نتائج")
                else -> ResultList(state.results, onOpenLesson, onOpenQuestion, onOpenFlashcard)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit, onClear: () -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        placeholder = { Text("ابحث في الدروس والأسئلة والبطاقات") },
        leadingIcon = { Icon(Icons.Rounded.Search, null) },
        trailingIcon = {
            if (query.isNotEmpty()) IconButton(onClick = onClear) { Icon(Icons.Rounded.Close, "مسح") }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun ResultList(
    results: SearchResults,
    onOpenLesson: (String) -> Unit,
    onOpenQuestion: (String) -> Unit,
    onOpenFlashcard: (String) -> Unit
) {
    LazyColumn(
        Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        if (results.lessons.isNotEmpty()) {
            item { GroupHeader("الدروس", results.lessons.size) }
            items(results.lessons, key = { it.id }) {
                ResultRow(it.title, it.snippet, Icons.Rounded.MenuBook) { onOpenLesson(it.id) }
            }
        }
        if (results.questions.isNotEmpty()) {
            item { GroupHeader("الأسئلة", results.questions.size) }
            items(results.questions, key = { it.id }) {
                ResultRow(it.title, it.snippet, Icons.Rounded.Quiz) { onOpenQuestion(it.id) }
            }
        }
        if (results.flashcards.isNotEmpty()) {
            item { GroupHeader("البطاقات", results.flashcards.size) }
            items(results.flashcards, key = { it.id }) {
                ResultRow(it.title, it.snippet, Icons.Rounded.Style) { onOpenFlashcard(it.id) }
            }
        }
    }
}

@Composable
private fun GroupHeader(label: String, count: Int) {
    Text("$label ($count)", style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Right,
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp))
}

@Composable
private fun ResultRow(title: String, snippet: String, icon: ImageVector, onClick: () -> Unit) {
    SectionCard(Modifier.clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
                if (snippet.isNotBlank())
                    Text(snippet, style = MaterialTheme.typography.labelSmall, maxLines = 2,
                        overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth())
            }
            Spacer(Modifier.width(12.dp))
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun Hint(text: String) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Text(text, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.outline)
    }
}
