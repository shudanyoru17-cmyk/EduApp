package com.edu.app.feature.exam

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.edu.app.core.ui.AppTopBar
import com.edu.app.core.ui.SectionCard
import com.edu.app.domain.model.Difficulty
import com.edu.app.domain.model.ExamResult

@Composable
fun ExamResultScreen(
    result: ExamResult,
    onRetry: () -> Unit,
    onHome: () -> Unit
) {
    NoDistractionsMode(enabled = false)
    Scaffold(topBar = { AppTopBar("النتيجة") }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionCard {
                val color = if (result.passed) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.error
                Text(if (result.passed) "ناجح 🎉" else "تحتاج للمراجعة",
                    style = MaterialTheme.typography.titleLarge, color = color)
                Spacer(Modifier.height(8.dp))
                Text("${result.scorePercent}%", style = MaterialTheme.typography.titleLarge, color = color)
                LinearProgressIndicator(progress = { result.scorePercent / 100f },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp), color = color)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCell("صحيحة", result.correct, Modifier.weight(1f))
                StatCell("خاطئة", result.incorrect, Modifier.weight(1f))
                StatCell("متروكة", result.unanswered, Modifier.weight(1f))
            }
            SectionCard {
                Text("الوقت المستغرق", style = MaterialTheme.typography.labelSmall)
                Text("%02d:%02d".format(result.timeTakenSeconds / 60, result.timeTakenSeconds % 60),
                    style = MaterialTheme.typography.titleMedium)
            }
            SectionCard {
                Text("التحليل حسب المستوى", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                result.byDifficulty.forEach { (diff, b) ->
                    val pct = if (b.total == 0) 0 else b.correct * 100 / b.total
                    Text(difficultyLabel(diff), textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth())
                    LinearProgressIndicator(progress = { pct / 100f },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp))
                    Text("$pct%  (${b.correct}/${b.total})", style = MaterialTheme.typography.labelSmall)
                }
                weakestArea(result)?.let {
                    Spacer(Modifier.height(8.dp))
                    Text("ننصح بمراجعة مستوى: ${difficultyLabel(it)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error)
                }
            }
            Button(onClick = onRetry, modifier = Modifier.fillMaxWidth()) { Text("إعادة") }
            TextButton(onClick = onHome, modifier = Modifier.fillMaxWidth()) { Text("العودة للرئيسية") }
        }
    }
}

@Composable
private fun StatCell(label: String, value: Int, modifier: Modifier) {
    SectionCard(modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Text("$value", style = MaterialTheme.typography.titleLarge)
    }
}

private fun difficultyLabel(d: Difficulty) = when (d) {
    Difficulty.EASY -> "سهل"; Difficulty.MEDIUM -> "متوسط"; Difficulty.HARD -> "صعب"
}

private fun weakestArea(result: ExamResult): Difficulty? =
    result.byDifficulty.filterValues { it.total > 0 }
        .minByOrNull { (_, b) -> b.correct.toFloat() / b.total }?.key
