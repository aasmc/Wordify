package ru.aasmc.wordify.common.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.aasmc.wordify.common.core.data.preferences.WordifyPreferences
import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PreferencesModule {

    @Singleton
    @Binds
    fun providePreferences(preferences: WordifyPreferences): PreferencesRepository

}