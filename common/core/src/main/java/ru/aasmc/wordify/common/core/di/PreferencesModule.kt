package ru.aasmc.wordify.common.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.aasmc.constants.PreferencesConstants
import ru.aasmc.wordify.common.core.data.preferences.WordifyPreferences
import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = PreferencesConstants.DATASTORE_NAME)

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    abstract fun providePreferences(preferences: WordifyPreferences): PreferencesRepository

    companion object {
        @Singleton
        @Provides
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.dataStore
        }
    }

}