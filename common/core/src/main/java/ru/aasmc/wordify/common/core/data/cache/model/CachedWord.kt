package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.*
import ru.aasmc.wordify.common.core.domain.model.Syllable
import ru.aasmc.wordify.common.core.domain.model.Word

@Entity(tableName = "words")
data class CachedWord(
    @PrimaryKey
    @ColumnInfo(name = "wordId")
    val wordId: String,
    val pronunciation: String,
    @Embedded
    val syllable: CachedSyllable,
    val frequency: Float = 0f,
    val isFavourite: Boolean = false,
    val timeAdded: Long
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
) {
    companion object {
        fun toDomain(cachedWordAggregate: CachedWordAggregate): Word {
            val cachedWord = cachedWordAggregate.cachedWord
            val cachedProps = cachedWordAggregate.wordProperties
            return Word(
                wordId = cachedWord.wordId,
                wordProperties = cachedProps.map { CachedWordPropertiesAggregate.toDomain(it) },
                syllable = Syllable(
                    count = cachedWord.syllable.count,
                    syllableList = cachedWord.syllable.syllables
                ),
                pronunciation = cachedWord.pronunciation,
                isFavourite = cachedWord.isFavourite,
                timeAdded = cachedWord.timeAdded
            )
        }

        fun fromDomain(word: Word): CachedWordAggregate {
            return CachedWordAggregate(
                cachedWord = CachedWord(
                    wordId = word.wordId,
                    pronunciation = word.pronunciation,
                    syllable = CachedSyllable(
                        count = word.syllable.count,
                        syllables = word.syllable.syllableList
                    ),
                    isFavourite = word.isFavourite,
                    timeAdded = word.timeAdded
                ),
                wordProperties = word.wordProperties.map { props ->
                    CachedWordPropertiesAggregate.fromDomain(
                        wordProperties = props,
                        word = word.wordId
                    )
                }
            )
        }
    }
}

















