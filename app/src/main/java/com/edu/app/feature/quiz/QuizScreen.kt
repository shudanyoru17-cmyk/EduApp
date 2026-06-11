package com.edu.app.feature.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.edu.app.core.ui.AppTopBar
import com.edu.app.core.ui.SectionCard

/** Skeleton quiz screen — wire to the question pool + a QuizViewModel to add logic. */
@Composable
fun QuizScreen(onFinish: () -> Unit) {
    Scaffold(topBar = { AppTopBar("اختبار") }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LinearProgressIndicator(progress = { 0f }, modifier = Modifier.fillMaxWidth())
            Text("السؤال ١ من ١٠", style = MaterialTheme.typography.labelSmall)
            SectionCard { Text("نص السؤال يظهر هنا", style = MaterialTheme.typography.titleMedium) }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(4) { i ->
                    OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)) {
                        Text("الخيار ${i + 1}", modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right)
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            Button(onClick = onFinish, modifier = Modifier.fillMaxWidth()) { Text("إنهاء") }
        }
    }
}
