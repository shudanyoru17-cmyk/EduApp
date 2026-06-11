package com.edu.app.di

import com.edu.app.core.common.Clock
import com.edu.app.core.common.IdGenerator
import com.edu.app.core.common.IoDispatcher
import com.edu.app.data.local.dao.*
import com.edu.app.data.repository.*
import com.edu.app.data.seed.LessonSeeder
import com.edu.app.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides @Singleton fun lessonSeeder() = LessonSeeder()

    @Provides @Singleton
    fun lessonRepo(dao: LessonDao, seeder: LessonSeeder): LessonRepository =
        LessonRepositoryImpl(dao, seeder)

    @Provides @Singleton
    fun examRepo(dao: ExamDao, ids: IdGenerator): ExamRepository =
        ExamRepositoryImpl(dao, ids)

    @Provides @Singleton
    fun flashRepo(dao: FlashcardDao, clock: Clock, ids: IdGenerator): FlashcardRepository =
        FlashcardRepositoryImpl(dao, clock, ids)

    @Provides @Singleton
    fun searchRepo(dao: SearchDao, @IoDispatcher io: CoroutineDispatcher): SearchRepository =
        SearchRepositoryImpl(dao, io)

    @Provides @Singleton
    fun progressRepo(dao: ProgressDao, clock: Clock): ProgressRepository =
        ProgressRepositoryImpl(dao, clock)
}
