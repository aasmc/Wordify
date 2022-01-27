package ru.aasmc.wordify.common.core.data.api.model.mappers

import ru.aasmc.wordify.common.core.data.api.model.WordDto
import ru.aasmc.wordify.common.core.data.api.model.WordPropertiesDto
import ru.aasmc.wordify.common.core.data.cache.model.*
import javax.inject.Inject

class WordDtoMapper @Inject constructor() : ApiMapper<WordDto?, CachedWordAggregate> {

    override fun mapToCache(dto: WordDto?): CachedWordAggregate {
        return CachedWordAggregate(
            cachedWord = CachedWord(
                wordId = dto?.word ?: throw MappingException("Word Id cannot be null"),
                pronunciation = dto.pronunciationDto?.all.orEmpty(),
                syllable = CachedSyllable(
                    count = dto.syllables.count ?: 0,
                    syllables = dto.syllables.syllableList.orEmpty().map { it.orEmpty() }
                ),
                frequency = dto.frequency ?: 0f
            ),
            wordProperties = parseWordProperties(dto)
        )
    }

    private fun parseWordProperties(dto: WordDto?): List<CachedWordPropertiesAggregate> {
        val wordId = dto?.word ?: throw MappingException("Word Id cannot be null")
        return dto.wordProperties?.map { parseSingleWordProperty(it, wordId) }.orEmpty()
    }

    private fun parseSingleWordProperty(
        dtoProperty: WordPropertiesDto?,
        wordId: String
    ): CachedWordPropertiesAggregate {
        return CachedWordPropertiesAggregate(

            cachedWordProperties = CachedWordProperties(
                wordId = wordId,
                definition = dtoProperty?.definition.orEmpty(),
                partOfSpeech = dtoProperty?.partOfSpeech.orEmpty(),
            ),
            synonyms = parseSynonyms(dtoProperty?.synonyms),
            derivations = parseDerivations(dtoProperty?.derivations),
            examples = parseExamples(dtoProperty?.examples)
        )

    }

    private fun parseSynonyms(synonyms: List<String?>?): List<CachedSynonym> {
        return synonyms?.map { parseSingleSynonym(it) }.orEmpty()
    }

    private fun parseSingleSynonym(synonym: String?): CachedSynonym {
        return CachedSynonym(
            synonym = synonym.orEmpty()
        )
    }

    private fun parseDerivations(derivations: List<String?>?): List<CachedDerivation> {
        return derivations?.map { parseSingleDerivation(it) }.orEmpty()
    }

    private fun parseSingleDerivation(derivation: String?): CachedDerivation {
        return CachedDerivation(
            derivation = derivation.orEmpty()
        )
    }

    private fun parseExamples(examples: List<String?>?): List<CachedExample> {
        return examples?.map { parseSingleExample(it) }.orEmpty()
    }

    private fun parseSingleExample(example: String?): CachedExample {
        return CachedExample(
            example = example.orEmpty()
        )
    }

}
























