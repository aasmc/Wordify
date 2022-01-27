package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.*

@Entity(tableName = "cachedWordProperties")
data class CachedWordProperties(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "propertiesId")
    val propertiesId: Long = 0,
    @ColumnInfo(name = "wordId")
    val wordId: String,
    @ColumnInfo(name = "definition")
    val definition: String,
    @ColumnInfo(name = "partOfSpeech")
    val partOfSpeech: String
)

@Entity(
    tableName = "propertiesSynonymCrossRef",
    primaryKeys = ["propertiesId", "synonymId"]
)
data class PropertiesSyllableCrossRef(
    @ColumnInfo(name = "propertiesId")
    val propertiesId: Long,
    @ColumnInfo(name = "synonymId")
    val synonymId: Long,
)

@Entity(
    tableName = "propertiesDerivationCrossRef",
    primaryKeys = ["propertiesId", "derivationId"]
)
data class PropertiesDerivationCrossRef(
    @ColumnInfo(name = "propertiesId")
    val propertiesId: Long,
    @ColumnInfo(name = "derivationId")
    val derivationId: Long,
)

@Entity(
    tableName = "propertiesExampleCrossRef",
    primaryKeys = ["propertiesId", "exampleId"]
)
data class PropertiesExampleCrossRef(
    @ColumnInfo(name = "propertiesId")
    val propertiesId: Long,
    @ColumnInfo(name = "exampleId")
    val exampleId: Long,
)


data class CachedWordPropertiesAggregate(
    @Embedded
    val cachedWordProperties: CachedWordProperties,
    @Relation(
        parentColumn = "propertiesId",
        entityColumn = "synonymId",
        associateBy = Junction(PropertiesSyllableCrossRef::class)
    )
    val synonyms: List<CachedSynonym>,
    @Relation(
        parentColumn = "propertiesId",
        entityColumn = "derivationId",
        associateBy = Junction(PropertiesDerivationCrossRef::class)
    )
    val derivations: List<CachedDerivation>,
    @Relation(
        parentColumn = "propertiesId",
        entityColumn = "exampleId",
        associateBy = Junction(PropertiesExampleCrossRef::class)
    )
    val examples: List<CachedExample>
)











