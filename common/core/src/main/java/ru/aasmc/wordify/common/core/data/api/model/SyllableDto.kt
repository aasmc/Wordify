package ru.aasmc.wordify.common.core.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SyllableDto(
    @field:Json(name = "count") val count: Int?,
    @field:Json(name = "list") val syllableList: List<String?>?
)