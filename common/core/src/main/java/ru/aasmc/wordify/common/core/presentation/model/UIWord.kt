package ru.aasmc.wordify.common.core.presentation.model

import ru.aasmc.wordify.common.core.domain.model.Word

data class UIWord(
    val wordId: String,
    val wordProperties: List<UIWordProperties>,
    val syllable: UISyllable,
    val pronunciation: String,
    val isFavourite: Boolean = false
) {
    companion object {
        fun fromDomain(word: Word): UIWord {
            return UIWord(
                wordId = word.wordId,
                wordProperties = word.wordProperties.map {
                    UIWordProperties.fromDomain(it)
                },
                syllable = UISyllable.fromDomain(word.syllable),
                pronunciation = "Pronunciation: [ ${word.pronunciation} ]",
                isFavourite = word.isFavourite
            )
        }
    }
}
