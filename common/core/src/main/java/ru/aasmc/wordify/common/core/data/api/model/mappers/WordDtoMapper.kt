package ru.aasmc.wordify.common.core.data.api.model.mappers

import ru.aasmc.wordify.common.core.data.api.model.WordDto
import ru.aasmc.wordify.common.core.data.api.model.WordPropertiesDto
import ru.aasmc.wordify.common.core.data.cache.model.*
import java.time.Instant
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

class WordDtoMapper @Inject constructor() : ApiMapper<WordDto?, CachedWordAggregate> {

    override fun mapToCache(dto: WordDto?): CachedWordAggregate {
        val wordName = dto?.word ?: throw MappingException("Word Id cannot be null")
        val wordId = UUID.randomUUID().leastSignificantBits
        return CachedWordAggregate(
            cachedWord = CachedWord(
                wordId = wordId,
                wordName = wordName,
                pronunciation = dto.pronunciationDto?.all.orEmpty(),
                syllable = CachedSyllable(
                    count = dto.syllables?.count ?: 0,
                    syllables = dto.syllables?.syllableList.orEmpty().map { it.orEmpty() }
                ),
                frequency = dto.frequency ?: 0f,
                isFavourite = false,
                timeAdded = Instant.now().toEpochMilli()
            ),
            wordProperties = parseWordProperties(dto, wordId)
        )
    }

    private fun parseWordProperties(dto: WordDto?, wordId: Long): List<CachedWordPropertiesAggregate> {
        return dto?.wordProperties?.map { parseSingleWordProperty(it, wordId) }.orEmpty()
    }

    private fun parseSingleWordProperty(
        dtoProperty: WordPropertiesDto?,
        wordId: Long
    ): CachedWordPropertiesAggregate {
        var definition = dtoProperty?.definition
        if (definition != null) {
            definition = definition.replaceFirstChar {
                if (it.isLowerCase()) {
                    it.titlecase()
                } else {
                    it.toString()
                }
            }
        }
        val propsId: Long = UUID.randomUUID().leastSignificantBits
        return CachedWordPropertiesAggregate(
            cachedWordProperties = CachedWordProperties(
                propertiesId = propsId,
                wordId = wordId,
                definition = definition.orEmpty(),
                partOfSpeech = dtoProperty?.partOfSpeech.orEmpty(),
            ),
            synonyms = parseSynonyms(dtoProperty?.synonyms, propsId),
            derivations = parseDerivations(dtoProperty?.derivations, propsId),
            examples = parseExamples(dtoProperty?.examples, propsId)
        )

    }

    private fun parseSynonyms(synonyms: List<String?>?, propsId: Long): List<CachedSynonym> {
        return synonyms?.map { parseSingleSynonym(it, propsId) }.orEmpty()
    }

    private fun parseSingleSynonym(synonym: String?, propsId: Long): CachedSynonym {
        return CachedSynonym(
            synonymId = UUID.randomUUID().leastSignificantBits,
            synonym = synonym.orEmpty(),
            propertiesId = propsId
        )
    }

    private fun parseDerivations(derivations: List<String?>?, propsId: Long): List<CachedDerivation> {
        return derivations?.map { parseSingleDerivation(it, propsId) }.orEmpty()
    }

    private fun parseSingleDerivation(derivation: String?, propsId: Long): CachedDerivation {
        return CachedDerivation(
            derivationId = UUID.randomUUID().leastSignificantBits,
            derivation = derivation.orEmpty(),
            propertiesId = propsId
        )
    }

    private fun parseExamples(examples: List<String?>?, propsId: Long): List<CachedExample> {
        return examples?.map { parseSingleExample(it, propsId) }.orEmpty()
    }

    private fun parseSingleExample(example: String?, propsId: Long): CachedExample {
        return CachedExample(
            exampleId = UUID.randomUUID().leastSignificantBits,
            example = example.orEmpty(),
            propertiesId = propsId
        )
    }

}
























