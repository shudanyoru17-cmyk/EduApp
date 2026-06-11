package com.edu.app.feature.exam

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.app.domain.model.ExamConfig

@Composable
fun ExamRoute(
    onExitToHome: () -> Unit,
    viewModel: ExamViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    when (val s = state) {
        ExamUiState.Configuring -> ExamConfigScreen(onStart = viewModel::begin)
        ExamUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
        is ExamUiState.Running -> ExamRunnerScreen(
            state = s,
            onSelectOption = viewModel::selectOption,
            onShortAnswer = viewModel::setShortAnswer,
            onNext = viewModel::next,
            onPrevious = viewModel::previous,
            onFinish = { viewModel.finish() }
        )
        is ExamUiState.Finished -> ExamResultScreen(
            result = s.result,
            onRetry = { viewModel.begin(ExamConfig(null, s.result.total, 15)) },
            onHome = onExitToHome
        )
        is ExamUiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text(s.message, color = MaterialTheme.colorScheme.error)
        }
    }
}
