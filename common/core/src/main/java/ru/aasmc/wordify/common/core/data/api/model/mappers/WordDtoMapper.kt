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
                propertiesId = wordId,
                wordId = wordId,
                definition = dtoProperty?.definition.orEmpty(),
                partOfSpeech = dtoProperty?.partOfSpeech.orEmpty(),
            ),
            synonyms = parseSynonyms(dtoProperty?.synonyms, wordId),
            derivations = parseDerivations(dtoProperty?.derivations, wordId),
            examples = parseExamples(dtoProperty?.examples, wordId)
        )

    }

    private fun parseSynonyms(synonyms: List<String?>?, propsId: String): List<CachedSynonym> {
        return synonyms?.map { parseSingleSynonym(it, propsId) }.orEmpty()
    }

    private fun parseSingleSynonym(synonym: String?, propsId: String): CachedSynonym {
        return CachedSynonym(
            synonym = synonym.orEmpty(),
            propertiesId = propsId
        )
    }

    private fun parseDerivations(derivations: List<String?>?, propsId: String): List<CachedDerivation> {
        return derivations?.map { parseSingleDerivation(it, propsId) }.orEmpty()
    }

    private fun parseSingleDerivation(derivation: String?, propsId: String): CachedDerivation {
        return CachedDerivation(
            derivation = derivation.orEmpty(),
            propertiesId = propsId
        )
    }

    private fun parseExamples(examples: List<String?>?, propsId: String): List<CachedExample> {
        return examples?.map { parseSingleExample(it, propsId) }.orEmpty()
    }

    private fun parseSingleExample(example: String?, propsId: String): CachedExample {
        return CachedExample(
            example = example.orEmpty(),
            propertiesId = propsId
        )
    }

}
























