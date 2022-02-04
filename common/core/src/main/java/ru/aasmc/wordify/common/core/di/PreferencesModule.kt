package ru.aasmc.wordify.common.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.aasmc.wordify.common.core.data.preferences.WordifyPreferences
import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    abstract fun providePreferences(preferences: WordifyPreferences): PreferencesRepository
}