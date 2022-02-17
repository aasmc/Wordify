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
            wordName = "word $id",
            wordProperties = createWordProperties(id),
            syllable = Syllable(
                count = id,
                syllableList = List(id) { "${it + 1}" }
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
            synonyms = List(index) { "${it + 1}" },
            derivation = List(index) { "${it + 1}" },
            examples = List(index) { "${it + 1}" }
        )
    }
}