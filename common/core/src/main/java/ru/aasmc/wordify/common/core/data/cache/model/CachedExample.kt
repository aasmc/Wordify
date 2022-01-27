package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cachedExample")
data class CachedExample(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exampleId")
    val exampleId: Long = 0,
    @ColumnInfo(name = "example")
    val example: String
)
