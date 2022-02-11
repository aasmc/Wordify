package ru.aasmc.wordify.common.core.presentation.model

import ru.aasmc.wordify.common.core.domain.model.Word

data class UIWord(
    val wordId: Long,
    val wordName: String,
    val wordProperties: List<UIWordProperties>,
    val syllable: UISyllable,
    val pronunciation: String,
    val isFavourite: Boolean = false
) {
    companion object {
        fun fromDomain(word: Word): UIWord {
            val uiWordName = word.wordName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }
                return UIWord(
                wordId = word.wordId,
                wordName = uiWordName,
                wordProperties = word.wordProperties.map {
                    UIWordProperties.fromDomain(it)
                },
                syllable = UISyllable.fromDomain(word.syllable),
                pronunciation = word.pronunciation,
                isFavourite = word.isFavourite
            )
        }
    }
}
