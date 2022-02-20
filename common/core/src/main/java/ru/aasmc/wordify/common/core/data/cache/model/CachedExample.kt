package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.*

@Entity(
    tableName = "cachedExample",
    foreignKeys = [
        ForeignKey(
            entity = CachedWordProperties::class,
            parentColumns = ["propertiesId"],
            childColumns = ["propertiesId"],
            onDelete = ForeignKey.CASCADE // delete this entity if the parent gets deleted
        )
    ],
    indices = [Index("propertiesId")]
)
data class CachedExample(
    @PrimaryKey
    @ColumnInfo(name = "exampleId")
    val exampleId: Long,
    @ColumnInfo(name = "propertiesId")
    val propertiesId: Long,
    @ColumnInfo(name = "example")
    val example: String
)
