package ru.aasmc.wordify.common.core.fakes

import ru.aasmc.wordify.common.core.domain.model.Syllable
import ru.aasmc.wordify.common.core.domain.model.Word
import ru.aasmc.wordify.common.core.domain.model.WordProperties
import java.time.Instant
import java.util.*

object FakeDomainWordFactory {
    fun createDomainWord(id: Int): Word {
        val wordId = UUID.randomUUID().leastSignificantBits
        return Word(
            wordId = wordId,
            wordName = "$id",
            wordProperties = createWordProperties(id),
            syllable = Syllable(
                count = id,
                syllableList = List(id) { "$it" }
            ),
            pronunciation = "$id",
            timeAdded = Instant.now().toEpochMilli()
        )
    }

    private fun createWordProperties(id: Int): List<WordProperties> {
        return (0 until id).map { createWordProperty(id) }
    }

    private fun createWordProperty(index: Int): WordProperties {
        return WordProperties(
            id = index.toLong(),
            definition = "$index",
            partOfSpeech = "$index",
            synonyms = List(index) { "$it" },
            derivation = List(index) { "$it" },
            examples = List(index) { "$it" }
        )
    }
}