package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.*

@Entity(
    tableName = "cachedSynonyms",
    foreignKeys = [
        ForeignKey(
            entity = CachedWordProperties::class,
            parentColumns = ["propertiesId"],
            childColumns = ["propertiesId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("propertiesId")]
)
data class CachedSynonym(
    @PrimaryKey
    @ColumnInfo(name = "synonymId")
    val synonymId: Long,
    @ColumnInfo(name = "propertiesId")
    val propertiesId: Long,
    @ColumnInfo(name = "synonym")
    val synonym: String
)