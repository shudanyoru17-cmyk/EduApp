package com.edu.app.data.seed

import com.edu.app.core.common.IoDispatcher
import com.edu.app.domain.repository.LessonRepository
import com.edu.app.domain.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppInitializer @Inject constructor(
    private val lessonRepo: LessonRepository,
    private val searchRepo: SearchRepository,
    @IoDispatcher private val io: CoroutineDispatcher
) {
    suspend fun runOnce() = withContext(io) {
        lessonRepo.seedIfEmpty()
        searchRepo.rebuildIndices()
    }
}
