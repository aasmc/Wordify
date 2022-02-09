package ru.aasmc.wordify.common.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import ru.aasmc.wordify.common.core.data.WordifyWordRepository
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import ru.aasmc.wordify.common.core.utils.CoroutineDispatchersProvider
import ru.aasmc.wordify.common.core.utils.DispatchersProvider
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
interface ActivityRetainedModule {
    @Binds
    @ActivityRetainedScoped
    fun bindWordRepository(
        wordRepository: WordifyWordRepository
    ): WordRepository

    @Binds
    fun bindDispatchersProvider(
        dispatchersProvider: CoroutineDispatchersProvider
    ): DispatchersProvider
}