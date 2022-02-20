package ru.aasmc.wordify.common.core.di

import android.content.Context
import android.content.SharedPreferences
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

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Singleton
    @Binds
    abstract fun providePreferences(preferences: WordifyPreferences): PreferencesRepository

    companion object {
        @Provides
        @Singleton
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
            return context.getSharedPreferences(
                PreferencesConstants.SHARED_PREFS_NAME,
                Context.MODE_PRIVATE
            )
        }
    }

}

