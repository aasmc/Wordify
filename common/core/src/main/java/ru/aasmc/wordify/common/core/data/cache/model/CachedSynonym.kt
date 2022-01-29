package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.*

@Entity(
    tableName = "cachedSynonyms",
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
data class CachedSynonym(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "synonymId")
    val synonymId: Long = 0,
    @ColumnInfo(name = "propertiesId")
    val propertiesId: String,
    @ColumnInfo(name = "synonym")
    val synonym: String
)