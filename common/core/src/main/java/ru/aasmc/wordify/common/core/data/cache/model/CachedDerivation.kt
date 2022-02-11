package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.*

@Entity(
    tableName = "cachedDerivation",
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
data class CachedDerivation(
    @PrimaryKey
    @ColumnInfo(name = "derivationId")
    val derivationId: Long,
    @ColumnInfo(name = "propertiesId")
    val propertiesId: Long,
    @ColumnInfo(name = "derivation")
    val derivation: String
)
