package com.edu.app.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edu.app.core.ui.AppTopBar
import com.edu.app.core.ui.SectionCard

@Composable
fun HomeScreen(
    onOpenLessons: () -> Unit,
    onOpenExam: () -> Unit,
    onOpenSearch: () -> Unit
) {
    Scaffold(
        topBar = { AppTopBar("الرئيسية") },
        floatingActionButton = {
            FloatingActionButton(onClick = onOpenSearch) { Icon(Icons.Rounded.Search, "بحث") }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionCard {
                Text("مرحباً بك 👋", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text("تابع التعلم من حيث توقفت", style = MaterialTheme.typography.bodyLarge)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SectionCard(Modifier.weight(1f).clickable(onClick = onOpenLessons)) { Text("الدروس") }
                SectionCard(Modifier.weight(1f).clickable(onClick = onOpenExam)) { Text("الامتحان") }
            }
            Text("ابدأ رحلتك التعليمية اليوم", style = MaterialTheme.typography.titleMedium)
        }
    }
}
