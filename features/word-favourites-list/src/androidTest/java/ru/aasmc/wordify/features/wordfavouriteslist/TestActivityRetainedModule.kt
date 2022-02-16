package ru.aasmc.wordify.features.wordfavouriteslist

import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.testing.TestInstallIn
import ru.aasmc.wordify.common.core.di.ActivityRetainedModule
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import ru.aasmc.wordify.common.core.fakes.FakeWordifyRepository
import ru.aasmc.wordify.common.core.utils.CoroutineDispatchersProvider
import ru.aasmc.wordify.common.core.utils.DispatchersProvider

@Module
@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [ActivityRetainedModule::class]
)
interface TestActivityRetainedModule {
    @Binds
    @ActivityRetainedScoped
    fun bindWordRepository(
        wordRepository: FakeWordifyRepository
    ): WordRepository

    @Binds
    fun bindDispatchersProvider(
        dispatchersProvider: CoroutineDispatchersProvider
    ): DispatchersProvider
}