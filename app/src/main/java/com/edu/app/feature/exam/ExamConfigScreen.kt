package com.edu.app.feature.exam

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edu.app.core.ui.AppTopBar
import com.edu.app.core.ui.SectionCard
import com.edu.app.domain.model.ExamConfig

@Composable
fun ExamConfigScreen(onStart: (ExamConfig) -> Unit) {
    var count by remember { mutableIntStateOf(10) }
    var minutes by remember { mutableIntStateOf(15) }
    Scaffold(topBar = { AppTopBar("محاكاة الامتحان") }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionCard {
                Text("عدد الأسئلة: $count", style = MaterialTheme.typography.titleMedium)
                Slider(value = count.toFloat(), valueRange = 5f..40f,
                    onValueChange = { count = it.toInt() })
            }
            SectionCard {
                Text("المدة: $minutes دقيقة", style = MaterialTheme.typography.titleMedium)
                Slider(value = minutes.toFloat(), valueRange = 5f..90f,
                    onValueChange = { minutes = it.toInt() })
            }
            Spacer(Modifier.weight(1f))
            Button(onClick = { onStart(ExamConfig(null, count, minutes)) },
                modifier = Modifier.fillMaxWidth()) { Text("ابدأ الامتحان") }
        }
    }
}
