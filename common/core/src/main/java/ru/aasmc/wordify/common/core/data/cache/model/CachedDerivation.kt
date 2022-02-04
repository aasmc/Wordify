package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.*

@Entity(
    tableName = "cachedDerivation",
//    foreignKeys = [
//        ForeignKey(
//            entity = CachedWordProperties::class,
//            parentColumns = ["propertiesId"],
//            childColumns = ["propertiesId"],
//            onDelete = ForeignKey.CASCADE // delete this entity if the parent gets deleted
//        )
//    ],
//    indices = [Index("propertiesId")]
)
data class CachedDerivation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "derivationId")
    val derivationId: Long = 0,
    @ColumnInfo(name = "propertiesId")
    val propertiesId: String,
    @ColumnInfo(name = "derivation")
    val derivation: String
)