package ru.aasmc.wordify.common.core.domain.model

data class Word(
    val name: String,
    val wordProperties: List<WordProperties>,
    val syllable: Syllable,
    val pronunciation: String,
    val isFavourite: Boolean = false,
    val timeAdded: Long
)
