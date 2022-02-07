package ru.aasmc.wordify.common.core.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.aasmc.wordify.common.core.data.cache.Cache
import ru.aasmc.wordify.common.core.data.cache.RoomCache
import ru.aasmc.wordify.common.core.data.cache.WordifyDatabase
import ru.aasmc.wordify.common.core.data.cache.dao.WordDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Singleton
    @Binds
    abstract fun bindCache(cache: RoomCache): Cache

    companion object {
        @Singleton
        @Provides
        fun provideDatabase(@ApplicationContext context: Context): WordifyDatabase {
            return Room.databaseBuilder(context, WordifyDatabase::class.java, "wordify.db")
                .build()
        }

        @Provides
        fun provideWordDao(wordifyDatabase: WordifyDatabase): WordDao =
            wordifyDatabase.wordDao()
    }
}