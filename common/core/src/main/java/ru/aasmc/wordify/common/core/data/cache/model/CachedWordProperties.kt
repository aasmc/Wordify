package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.*
import ru.aasmc.wordify.common.core.domain.model.WordProperties

@Entity(
    tableName = "cachedWordProperties",
    foreignKeys = [
        ForeignKey(
            entity = CachedWord::class,
            parentColumns = ["wordId"],
            childColumns = ["wordId"],
        )
    ],
    indices = [Index("wordId")]
)
data class CachedWordProperties(
    @PrimaryKey
    @ColumnInfo(name = "propertiesId")
    val propertiesId: Long,
    @ColumnInfo(name = "wordId")
    val wordId: Long,
    @ColumnInfo(name = "definition")
    val definition: String,
    @ColumnInfo(name = "partOfSpeech")
    val partOfSpeech: String
)


data class CachedWordPropertiesAggregate(
    @Embedded
    val cachedWordProperties: CachedWordProperties,
    @Relation(
        parentColumn = "propertiesId",
        entityColumn = "propertiesId",
    )
    val synonyms: List<CachedSynonym>,
    @Relation(
        parentColumn = "propertiesId",
        entityColumn = "propertiesId",
    )
    val derivations: List<CachedDerivation>,
    @Relation(
        parentColumn = "propertiesId",
        entityColumn = "propertiesId",
    )
    val examples: List<CachedExample>
) {
    companion object {
        fun toDomain(cachedProperties: CachedWordPropertiesAggregate): WordProperties {
            return WordProperties(
                id = cachedProperties.cachedWordProperties.propertiesId,
                definition = cachedProperties.cachedWordProperties.definition,
                partOfSpeech = cachedProperties.cachedWordProperties.partOfSpeech,
                synonyms = cachedProperties.synonyms.map { it.synonym },
                derivation = cachedProperties.derivations.map { it.derivation },
                examples = cachedProperties.examples.map { it.example }
            )
        }
    }
}











