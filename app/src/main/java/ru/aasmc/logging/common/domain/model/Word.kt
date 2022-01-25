package ru.aasmc.logging.common.domain.model

data class Word(
    val name: String,
    val wordProperties: List<WordProperties>,
    val syllable: Syllable,
    val pronunciation: Pronunciation,
)
