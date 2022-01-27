package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.*

data class CachedSyllable(
    @ColumnInfo(name = "count")
    val count: Int,
    @ColumnInfo(name = "syllableList")
    val syllables: List<String>
)



