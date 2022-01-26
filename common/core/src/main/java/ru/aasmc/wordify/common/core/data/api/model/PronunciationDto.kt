package ru.aasmc.wordify.common.core.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PronunciationDto(
    @field:Json(name = "all") val all: String?
)