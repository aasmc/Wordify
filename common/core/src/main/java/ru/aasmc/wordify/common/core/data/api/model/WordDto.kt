package ru.aasmc.wordify.common.core.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.aasmc.wordify.common.core.data.api.model.adapter.PronunciationDtoAdapter

@JsonClass(generateAdapter = true, generator = "PronunciationDtoAdapter")
data class WordDto(
    @field:Json(name = "word") val word: String?,
    @field:Json(name = "results") val wordProperties: List<WordPropertiesDto>?,
    @field:Json(name = "syllables") val syllables: SyllableDto?,
    @field:Json(name = "pronunciation") val pronunciationDto: PronunciationDto?,
    @field:Json(name = "frequency") val frequency: Float?,
)