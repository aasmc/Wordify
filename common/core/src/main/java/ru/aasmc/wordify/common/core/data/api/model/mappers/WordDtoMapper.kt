package ru.aasmc.wordify.common.core.data.api.model.mappers

import ru.aasmc.wordify.common.core.data.api.model.SyllableDto
import ru.aasmc.wordify.common.core.data.api.model.WordDto
import ru.aasmc.wordify.common.core.data.api.model.WordPropertiesDto
import ru.aasmc.wordify.common.core.domain.model.Syllable
import ru.aasmc.wordify.common.core.domain.model.Word
import ru.aasmc.wordify.common.core.domain.model.WordProperties
import javax.inject.Inject

class WordDtoMapper @Inject constructor(
    private val pronunciationDtoMapper: PronunciationDtoMapper,
    private val syllableDtoMapper: SyllableDtoMapper,
    private val wordPropertiesDtoMapper: WordPropertiesDtoMapper
) : ApiMapper<WordDto?, Word> {

    override fun mapToDomain(dto: WordDto?): Word {
        return Word(
            name = dto?.word.orEmpty(),
            wordProperties = parseWordProperties(dto?.wordProperties),
            syllable = syllableDtoMapper.mapToDomain(dto?.syllables),
            pronunciation = pronunciationDtoMapper.mapToDomain(dto?.pronunciationDto)
        )
    }

    private fun parseWordProperties(dtoProps: List<WordPropertiesDto>?): List<WordProperties> {
        return dtoProps?.map {
            wordPropertiesDtoMapper.mapToDomain(it)
        } ?: emptyList()
    }

}