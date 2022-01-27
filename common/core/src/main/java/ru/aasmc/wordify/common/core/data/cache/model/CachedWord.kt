package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.*

@Entity(tableName = "words")
data class CachedWord(
    @PrimaryKey
    @ColumnInfo(name = "wordId")
    val wordId: String,
    val pronunciation: String,
    @Embedded
    val syllable: CachedSyllable,
    val frequency: Float = 0f
)


data class CachedWordAggregate(
    @Embedded
    val cachedWord: CachedWord,
    @Relation(
        entity = CachedWordProperties::class,
        parentColumn = "wordId",
        entityColumn = "wordId"
    )
    val wordProperties: List<CachedWordPropertiesAggregate>
)