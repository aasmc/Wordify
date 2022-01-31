package ru.aasmc.wordify.common.core.data.api.model.mappers

import org.junit.Assert.*
import org.junit.Test
import ru.aasmc.wordify.common.core.fakes.FakeWordDtoFactory
import kotlin.random.Random

const val WORD_TO_CREATE = 1

class WordDtoMapperTest {
    private val dtoWord = FakeWordDtoFactory.createDtoWord(WORD_TO_CREATE)
    private val dtoMapper = WordDtoMapper()

    @Test
    fun mapToCache_correct() {
        val word = dtoMapper.mapToCache(dtoWord)
        assertEquals("$WORD_TO_CREATE", word.cachedWord.wordId)
        assertEquals(WORD_TO_CREATE, word.wordProperties.size)
        assertEquals(WORD_TO_CREATE, word.cachedWord.syllable.count)
        assertEquals("$WORD_TO_CREATE", word.cachedWord.pronunciation)
        assertFalse(word.cachedWord.isFavourite)

        // check properties correct
        val prop = word.wordProperties[0]
        assertTrue(prop.cachedWordProperties.definition == "$WORD_TO_CREATE")
        assertTrue(prop.cachedWordProperties.partOfSpeech == "$WORD_TO_CREATE")
        assertTrue(prop.synonyms.size == WORD_TO_CREATE)
        assertTrue(prop.derivations.size == WORD_TO_CREATE)
        assertTrue(prop.examples.size == WORD_TO_CREATE)

        // check lists correct
        val index = Random.nextInt(0, WORD_TO_CREATE)
        val synonym = prop.synonyms[index]
        val derivation = prop.derivations[index]
        val example = prop.examples[index]
        assertTrue(synonym.synonym == "$index")
        assertTrue(synonym.propertiesId == "$WORD_TO_CREATE")
        assertTrue(derivation.derivation == "$index")
        assertTrue(derivation.propertiesId == "$WORD_TO_CREATE")
        assertTrue(example.example == "$index")
        assertTrue(example.propertiesId == "$WORD_TO_CREATE")
    }

}


































