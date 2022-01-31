package ru.aasmc.wordify.common.core.fakes

import ru.aasmc.wordify.common.core.domain.model.Syllable
import ru.aasmc.wordify.common.core.domain.model.Word
import ru.aasmc.wordify.common.core.domain.model.WordProperties
import java.time.Instant

object FakeDomainWordFactory {
    fun createDomainWord(id: Int): Word {
        return Word(
            name = "$id",
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
            id = "$index",
            definition = "$index",
            partOfSpeech = "$index",
            synonyms = List(index) { "$it" },
            derivation = List(index) { "$it" },
            examples = List(index) { "$it" }
        )
    }
}