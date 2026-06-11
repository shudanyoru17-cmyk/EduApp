package com.edu.app.feature.progress

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.app.core.ui.AppTopBar
import com.edu.app.core.ui.SectionCard

@Composable
fun ProgressScreen(
    onOpenSearch: () -> Unit,
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { AppTopBar("تقدمي") },
        floatingActionButton = {
            FloatingActionButton(onClick = onOpenSearch) { Icon(Icons.Rounded.Search, "بحث") }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("الدروس المكتملة", stats?.completedLessons?.toString() ?: "—", Modifier.weight(1f))
                StatCard("متوسط الدرجات", stats?.let { "${it.averageExamScore}%" } ?: "—", Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("مراجعات البطاقات", stats?.totalReviews?.toString() ?: "—", Modifier.weight(1f))
                StatCard("بطاقات مستحقة", stats?.dueFlashcards?.toString() ?: "—", Modifier.weight(1f))
            }
            SectionCard {
                Text("سلسلة الأيام 🔥", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text("واصل المراجعة يومياً للحفاظ على تقدمك", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier) {
    SectionCard(modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Spacer(Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleLarge)
    }
}
