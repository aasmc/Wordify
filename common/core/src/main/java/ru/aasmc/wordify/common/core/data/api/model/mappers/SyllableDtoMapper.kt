package ru.aasmc.wordify.common.core.data.api.model.mappers

import ru.aasmc.wordify.common.core.data.api.model.SyllableDto
import ru.aasmc.wordify.common.core.domain.model.Syllable
import javax.inject.Inject

class SyllableDtoMapper @Inject constructor(): ApiMapper<SyllableDto?, Syllable> {

    override fun mapToDomain(dto: SyllableDto?): Syllable {
        return Syllable(
            count = dto?.count ?: 0,
            syllableList = dto?.syllableList.orEmpty().map { it.orEmpty() }
        )
    }

}