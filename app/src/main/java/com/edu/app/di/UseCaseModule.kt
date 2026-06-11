package com.edu.app.di

import com.edu.app.domain.repository.*
import com.edu.app.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides fun getCategories(r: LessonRepository) = GetCategoriesUseCase(r)
    @Provides fun getLesson(r: LessonRepository) = GetLessonUseCase(r)
    @Provides fun startExam(r: ExamRepository) = StartExamUseCase(r)
    @Provides fun gradeExam() = GradeExamUseCase()
    @Provides fun startRevision(r: FlashcardRepository) = StartDailyRevisionUseCase(r)
    @Provides fun reviewCard(r: FlashcardRepository) = ReviewCardUseCase(r)
    @Provides fun observeDue(r: FlashcardRepository) = ObserveDueCountUseCase(r)
    @Provides fun search(r: SearchRepository) = SearchUseCase(r)
    @Provides fun getStats(r: ProgressRepository) = GetUserStatsUseCase(r)
}
