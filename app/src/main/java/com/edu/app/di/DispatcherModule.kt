package com.edu.app.di

import com.edu.app.core.common.Clock
import com.edu.app.core.common.DefaultDispatcher
import com.edu.app.core.common.IdGenerator
import com.edu.app.core.common.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @Provides @IoDispatcher fun io(): CoroutineDispatcher = Dispatchers.IO
    @Provides @DefaultDispatcher fun provideDefault(): CoroutineDispatcher = Dispatchers.Default
    @Provides @Singleton fun clock() = Clock()
    @Provides @Singleton fun ids() = IdGenerator()
}
