package ru.aasmc.wordify.common.core.data.api.model.mappers

import ru.aasmc.wordify.common.core.data.api.model.PronunciationDto
import ru.aasmc.wordify.common.core.domain.model.Pronunciation
import javax.inject.Inject

class PronunciationDtoMapper @Inject constructor() : ApiMapper<PronunciationDto?, Pronunciation> {

    override fun mapToDomain(dto: PronunciationDto?): Pronunciation {
        return Pronunciation(
            all = dto?.all.orEmpty()
        )
    }
}