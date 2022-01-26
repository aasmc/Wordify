package ru.aasmc.wordify.common.core.data.api.model.mappers

import ru.aasmc.wordify.common.core.data.api.model.WordPropertiesDto
import ru.aasmc.wordify.common.core.domain.model.WordProperties
import javax.inject.Inject

class WordPropertiesDtoMapper @Inject constructor() :
    ApiMapper<WordPropertiesDto?, WordProperties> {

    override fun mapToDomain(dto: WordPropertiesDto?): WordProperties {
        return WordProperties(
            definition = dto?.definition ?: throw MappingException("Word from network result cannot be null"),
            partOfSpeech = dto.partOfSpeech.orEmpty(),
            synonyms = dto.synonyms.orEmpty().map { it.orEmpty() },
            derivation = dto.derivations.orEmpty().map { it.orEmpty() },
            examples = dto.examples.orEmpty().map { it.orEmpty() }
        )
    }

}