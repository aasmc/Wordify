package ru.aasmc.wordify.common.domain.model

data class WordProperties(
    val definition: String,
    val partOfSpeech: String,
    val synonyms: List<String>,
    val derivation: List<String>,
    val examples: List<String>,
)
