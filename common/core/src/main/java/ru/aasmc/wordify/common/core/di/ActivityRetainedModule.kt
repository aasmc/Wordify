package ru.aasmc.wordify.common.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import ru.aasmc.wordify.common.core.data.WordifyWordRepository
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import ru.aasmc.wordify.common.core.utils.CoroutineDispatchersProvider
import ru.aasmc.wordify.common.core.utils.DispatchersProvider

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedModule {

    @Binds
    abstract fun bindDispatchersProvider(
        dispatchersProvider: CoroutineDispatchersProvider
    ): DispatchersProvider

    @Binds
    @ActivityRetainedScoped
    abstract fun bindWordRepository(
        wordRepository: WordifyWordRepository
    ): WordRepository

}