package com.edu.app.di

import android.content.Context
import androidx.room.Room
import com.edu.app.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun db(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "edu.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun lessonDao(db: AppDatabase) = db.lessonDao()
    @Provides fun examDao(db: AppDatabase) = db.examDao()
    @Provides fun flashcardDao(db: AppDatabase) = db.flashcardDao()
    @Provides fun searchDao(db: AppDatabase) = db.searchDao()
    @Provides fun progressDao(db: AppDatabase) = db.progressDao()
}
