package ru.aasmc.wordify.common.core.fakes

import ru.aasmc.wordify.common.core.data.cache.model.*

object AndroidFakeCachedWordFactory {

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

    private fun createWordProperty(index: Int): CachedWordPropertiesAggregate {
        return CachedWordPropertiesAggregate(
            cachedWordProperties = CachedWordProperties(
                propertiesId = "$index",
                wordId = "$index",
                definition = "$index",
                partOfSpeech = "$index"
            ),
            synonyms = List(index) { CachedSynonym(synonym = "$it", propertiesId = "$index") },
            derivations = List(index) { CachedDerivation(derivation = "$it", propertiesId = "$index") },
            examples = List(index) { CachedExample(example = "$it", propertiesId = "$index") }
        )
    }

}















