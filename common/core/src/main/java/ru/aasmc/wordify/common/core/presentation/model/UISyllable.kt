package ru.aasmc.wordify.common.core.presentation.model

import ru.aasmc.wordify.common.core.domain.model.Syllable

data class UISyllable(
    val count: Int,
    val syllableList: List<String>
) {
    companion object {
        fun fromDomain(syllable: Syllable): UISyllable {
            return UISyllable(
                count = syllable.count,
                syllableList = syllable.syllableList
            )
        }
    }
}
