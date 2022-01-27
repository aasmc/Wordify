package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cachedSynonyms")
data class CachedSynonym(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "synonymId")
    val synonymId: Long = 0,
    @ColumnInfo(name = "synonym")
    val synonym: String
)