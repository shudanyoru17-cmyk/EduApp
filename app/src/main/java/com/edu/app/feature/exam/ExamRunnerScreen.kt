package com.edu.app.feature.exam

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.edu.app.domain.model.QuestionType

@Composable
fun ExamRunnerScreen(
    state: ExamUiState.Running,
    onSelectOption: (String, String) -> Unit,
    onShortAnswer: (String, String) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onFinish: () -> Unit
) {
    NoDistractionsMode(enabled = true)
    var confirmExit by remember { mutableStateOf(false) }
    BackHandler { confirmExit = true }

    Column(Modifier.fillMaxSize().systemBarsPadding().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            TimerChip(state.remainingSeconds)
            Text("${state.index + 1} / ${state.questions.size}",
                style = MaterialTheme.typography.titleMedium)
        }
        LinearProgressIndicator(progress = { (state.index + 1f) / state.questions.size },
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))

        val q = state.current
        val answer = state.answers[q.id]
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Text(q.prompt, style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            when (q.type) {
                QuestionType.MCQ, QuestionType.TRUE_FALSE ->
                    q.options.forEach { opt ->
                        val selected = answer?.selectedOptionIds?.contains(opt.id) == true
                        OptionButton(opt.text, selected) { onSelectOption(q.id, opt.id) }
                    }
                QuestionType.SHORT_ANSWER ->
                    OutlinedTextField(
                        value = answer?.shortAnswerText.orEmpty(),
                        onValueChange = { onShortAnswer(q.id, it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("اكتب إجابتك") }
                    )
            }
        }

        Row(Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onPrevious, enabled = state.index > 0,
                modifier = Modifier.weight(1f)) { Text("السابق") }
            if (state.isLast) {
                Button(onClick = onFinish, modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary)) { Text("إنهاء") }
            } else {
                Button(onClick = onNext, modifier = Modifier.weight(1f)) { Text("التالي") }
            }
        }
    }

    if (confirmExit) {
        AlertDialog(
            onDismissRequest = { confirmExit = false },
            title = { Text("إنهاء الامتحان؟") },
            text = { Text("سيتم احتساب الأسئلة غير المجابة كخاطئة.") },
            confirmButton = { TextButton(onClick = { confirmExit = false; onFinish() }) { Text("إنهاء") } },
            dismissButton = { TextButton(onClick = { confirmExit = false }) { Text("متابعة") } }
        )
    }
}

@Composable
private fun TimerChip(remaining: Long) {
    val low = remaining <= 60
    val color = if (low) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    AssistChip(
        onClick = {},
        leadingIcon = { Icon(Icons.Rounded.Timer, null, tint = color) },
        label = { Text("%02d:%02d".format(remaining / 60, remaining % 60), color = color) }
    )
}

@Composable
private fun OptionButton(text: String, selected: Boolean, onClick: () -> Unit) {
    val border = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        border = BorderStroke(if (selected) 2.dp else 1.dp, border),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(text, modifier = Modifier.weight(1f), textAlign = TextAlign.Right)
        if (selected) Icon(Icons.Rounded.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
    }
}
