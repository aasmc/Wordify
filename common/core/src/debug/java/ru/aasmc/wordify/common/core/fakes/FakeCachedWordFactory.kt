package ru.aasmc.wordify.common.core.fakes

import ru.aasmc.wordify.common.core.data.cache.model.*
import java.time.Instant
import java.util.*

object FakeCachedWordFactory {

    fun createFavCacheWord(wordName: Int): CachedWordAggregate {
        val wordId = UUID.randomUUID().leastSignificantBits
        return CachedWordAggregate(
            cachedWord = CachedWord(
                wordId = wordId,
                wordName = "$wordName",
                pronunciation = "$wordName",
                syllable = CachedSyllable(
                    count = wordName,
                    syllables = List(wordName) { "$it" }
                ),
                frequency = wordName.toFloat(),
                isFavourite = true,
                timeAdded = Instant.now().toEpochMilli()
            ),
            wordProperties = createWordProperties(wordName, wordId)
        )
    }

    fun createCachedWord(wordName: Int): CachedWordAggregate {
        val wordId = UUID.randomUUID().leastSignificantBits
        return CachedWordAggregate(
            cachedWord = CachedWord(
                wordId = wordId,
                wordName = "$wordName",
                pronunciation = "$wordName",
                syllable = CachedSyllable(
                    count = wordName,
                    syllables = List(wordName) { "$it" }
                ),
                frequency = wordName.toFloat(),
                isFavourite = false,
                timeAdded = Instant.now().toEpochMilli()
            ),
            wordProperties = createWordProperties(wordName, wordId)
        )
    }

    private fun createWordProperties(wordName: Int, wordId: Long): List<CachedWordPropertiesAggregate> {
        return (0 until wordName).map { createWordProperty(wordName, wordId) }
    }

    private fun createWordProperty(wordName: Int, wordId: Long): CachedWordPropertiesAggregate {
        val propsId = UUID.randomUUID().leastSignificantBits
        return CachedWordPropertiesAggregate(
            cachedWordProperties = CachedWordProperties(
                wordId = wordId,
                definition = "$wordName",
                partOfSpeech = "$wordName",
                propertiesId = propsId
            ),
            synonyms = List(wordName) { CachedSynonym(synonymId = UUID.randomUUID().leastSignificantBits, synonym = "$it", propertiesId = propsId) },
            derivations = List(wordName) {
                CachedDerivation(
                    derivationId = UUID.randomUUID().leastSignificantBits,
                    derivation = "$it",
                    propertiesId = propsId
                )
            },
            examples = List(wordName) { CachedExample(exampleId = UUID.randomUUID().leastSignificantBits, example = "$it", propertiesId = propsId) }
        )
    }
}















