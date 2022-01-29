package ru.aasmc.wordify.common.core.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.aasmc.wordify.common.core.data.cache.dao.WordDao
import ru.aasmc.wordify.common.core.data.cache.model.*

@Database(
    entities = [
        CachedDerivation::class,
        CachedExample::class,
        CachedSynonym::class,
        CachedWord::class,
        CachedWordProperties::class
    ],
    version = 1
)
@TypeConverters(SyllableConverter::class)
abstract class WordifyDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}