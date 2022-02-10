package ru.aasmc.wordify.common.core.fakes

import ru.aasmc.wordify.common.core.data.api.model.PronunciationDto
import ru.aasmc.wordify.common.core.data.api.model.SyllableDto
import ru.aasmc.wordify.common.core.data.api.model.WordDto
import ru.aasmc.wordify.common.core.data.api.model.WordPropertiesDto

object FakeWordDtoFactory {

    fun createDtoWord(id: Int): WordDto {
        return WordDto(
            word = "$id",
            wordProperties = createWordDtoProperties(id),
            syllables = createSyllableDto(id),
            pronunciationDto = PronunciationDto(all = "$id"),
            frequency = id.toFloat(),
        )
    }

    private fun createSyllableDto(id: Int): SyllableDto {
        return SyllableDto(
            count = id,
            syllableList = List(id) { "$it" }
        )
    }

    private fun createWordDtoProperties(id: Int): List<WordPropertiesDto> {
        return List(id) {
            WordPropertiesDto(
                definition = "$id",
                partOfSpeech = "$id",
                synonyms = List(id) { "$it" },
                typeOf = null,
                hasTypes = null,
                derivations = List(id) { "$it" },
                examples = List(id) { "$it" },
                inCategory = null,
                hasParts = null
            )
        }
    }


}













