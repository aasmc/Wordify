package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.*
import ru.aasmc.wordify.common.core.domain.model.WordProperties

@Entity(tableName = "cachedWordProperties")
data class CachedWordProperties(
    @PrimaryKey
    @ColumnInfo(name = "propertiesId")
    val propertiesId: String,
    @ColumnInfo(name = "wordId")
    val wordId: String,
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

        fun fromDomain(
            wordProperties: WordProperties,
            word: String
        ): CachedWordPropertiesAggregate {
            return CachedWordPropertiesAggregate(
                cachedWordProperties = CachedWordProperties(
                    propertiesId = word,
                    wordId = word,
                    definition = wordProperties.definition,
                    partOfSpeech = wordProperties.partOfSpeech
                ),
                synonyms = wordProperties.synonyms.map {
                    CachedSynonym(synonym = it, propertiesId = wordProperties.id)
                },
                derivations = wordProperties.derivation.map {
                    CachedDerivation(
                        derivation = it,
                        propertiesId = wordProperties.id
                    )
                },
                examples = wordProperties.examples.map {
                    CachedExample(
                        example = it,
                        propertiesId = wordProperties.id
                    )
                }
            )
        }
    }
}











