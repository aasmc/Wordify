package ru.aasmc.wordify.common.core.presentation.model

import ru.aasmc.wordify.common.core.domain.model.WordProperties

data class UIWordProperties(
    val definition: String,
    val partOfSpeech: String,
    val synonyms: List<String>,
    val derivation: List<String>,
    val examples: List<String>
) {
    companion object {
        fun fromDomain(wordProperties: WordProperties): UIWordProperties {
            return UIWordProperties(
                definition = wordProperties.definition,
                partOfSpeech = wordProperties.partOfSpeech,
                synonyms = wordProperties.synonyms,
                derivation = wordProperties.derivation,
                examples = wordProperties.examples
            )
        }
    }
}
