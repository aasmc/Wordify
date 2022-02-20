package ru.aasmc.wordify.common.core.domain.model

data class WordProperties(
    val id: Long,
    val definition: String,
    val partOfSpeech: String,
    val synonyms: List<String>,
    val derivation: List<String>,
    val examples: List<String>,
)
