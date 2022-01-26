package ru.aasmc.wordify.common.core.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WordPropertiesDto(
    @field:Json(name = "definition") val definition: String?,
    @field:Json(name = "partOfSpeech") val partOfSpeech: String?,
    @field:Json(name = "synonyms") val synonyms: List<String?>?,
    @field:Json(name = "typeOf") val typeOf: List<String?>?,
    @field:Json(name = "hasTypes") val hasTypes: List<String?>?,
    @field:Json(name = "partOf") val derivations: List<String?>?,
    @field:Json(name = "examples") val examples: List<String?>?,
    @field:Json(name = "inCategory") val inCategory: List<String?>?,
    @field:Json(name = "hasParts") val hasParts: List<String?>?
)