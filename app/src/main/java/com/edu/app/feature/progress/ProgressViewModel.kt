package com.edu.app.feature.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.app.domain.model.UserStats
import com.edu.app.domain.usecase.GetUserStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    getStats: GetUserStatsUseCase
) : ViewModel() {
    val stats: StateFlow<UserStats?> = getStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
