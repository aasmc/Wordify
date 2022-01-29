package ru.aasmc.wordify.common.core.data.fakes

import ru.aasmc.wordify.common.core.data.cache.model.*

object FakeCachedWordFactory {

    fun createCachedWord(id: Int): CachedWordAggregate {
        return CachedWordAggregate(
            cachedWord = CachedWord(
                wordId = "$id",
                pronunciation = "$id",
                syllable = CachedSyllable(
                    count = id,
                    syllables = List(id) { "$it" }
                ),
                frequency = id.toFloat()
            ),
            wordProperties = createWordProperties(id)
        )
    }

    private fun createWordProperties(id: Int): List<CachedWordPropertiesAggregate> {
        return (0 until id).map { createWordProperty(id) }
    }

    private fun createWordProperty( wordId: Int): CachedWordPropertiesAggregate {
        return CachedWordPropertiesAggregate(
            cachedWordProperties = CachedWordProperties(
                propertiesId = "$wordId",
                wordId = "$wordId",
                definition = "$wordId",
                partOfSpeech = "$wordId"
            ),
            synonyms = List(wordId) { CachedSynonym(synonym = "$it", propertiesId = "$wordId") },
            derivations = List(wordId) { CachedDerivation(derivation = "$it", propertiesId = "$wordId") },
            examples = List(wordId) { CachedExample(example = "$it", propertiesId = "$wordId") }
        )
    }

}















