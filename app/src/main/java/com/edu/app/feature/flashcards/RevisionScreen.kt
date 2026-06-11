package com.edu.app.feature.flashcards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.app.core.ui.AppTopBar
import com.edu.app.core.ui.SectionCard
import com.edu.app.domain.model.SessionSummary

@Composable
fun RevisionScreen(
    onExit: () -> Unit,
    viewModel: FlashcardsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.loadDailySession() }

    Scaffold(topBar = { AppTopBar("المراجعة اليومية") }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            when (val s = state) {
                RevisionUiState.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                RevisionUiState.Empty -> EmptyState(onExit)
                is RevisionUiState.Reviewing -> ReviewingContent(s, viewModel::reveal, viewModel::grade)
                is RevisionUiState.Done -> SummaryState(s.summary, onExit)
            }
        }
    }
}

@Composable
private fun ReviewingContent(
    state: RevisionUiState.Reviewing,
    onFlip: () -> Unit,
    onGrade: (com.edu.app.domain.srs.ReviewGrade) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Text("${state.position} / ${state.total}", style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        LinearProgressIndicator(progress = { state.position.toFloat() / state.total },
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
        FlipCard(state.current.front, state.current.back, state.current.hint,
            state.isRevealed, onFlip, Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))
        AnimatedVisibility(visible = state.isRevealed) { GradeBar(onGrade = onGrade) }
        AnimatedVisibility(visible = !state.isRevealed) {
            Button(onClick = onFlip, modifier = Modifier.fillMaxWidth()) { Text("عرض الإجابة") }
        }
    }
}

@Composable
private fun EmptyState(onExit: () -> Unit) {
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Text("🎉", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        Text("لا توجد بطاقات للمراجعة اليوم", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onExit) { Text("رجوع") }
    }
}

@Composable
private fun SummaryState(summary: SessionSummary, onExit: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("انتهت الجلسة ✅", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        SectionCard { Text("راجعت ${summary.reviewed} بطاقة", style = MaterialTheme.typography.titleMedium) }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Stat("نسيت", summary.again, Modifier.weight(1f))
            Stat("صعب", summary.hard, Modifier.weight(1f))
            Stat("جيد", summary.good, Modifier.weight(1f))
            Stat("سهل", summary.easy, Modifier.weight(1f))
        }
        Button(onClick = onExit, modifier = Modifier.fillMaxWidth()) { Text("تم") }
    }
}

@Composable
private fun Stat(label: String, value: Int, modifier: Modifier) {
    SectionCard(modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Text("$value", style = MaterialTheme.typography.titleMedium)
    }
}
