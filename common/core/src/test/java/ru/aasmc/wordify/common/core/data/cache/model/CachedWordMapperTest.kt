package ru.aasmc.wordify.common.core.data.cache.model

import org.junit.Assert.*
import org.junit.Test
import ru.aasmc.wordify.common.core.domain.model.Word
import ru.aasmc.wordify.common.core.fakes.FakeCachedWordFactory
import ru.aasmc.wordify.common.core.fakes.FakeDomainWordFactory
import kotlin.random.Random

const val WORD_TO_CREATE = 1

class CachedWordMapperTest {
    private val cachedWord = FakeCachedWordFactory.createCachedWord(WORD_TO_CREATE)

    @Test
    fun mapToDomain_correct() {
        val word: Word = CachedWordAggregate.toDomain(cachedWord)
        // check size and word
        assertEquals("$WORD_TO_CREATE", word.wordName)
        assertTrue(word.wordProperties.size == WORD_TO_CREATE)
        assertTrue(word.syllable.count == WORD_TO_CREATE)
        assertTrue(word.pronunciation == "$WORD_TO_CREATE")
        assertTrue(word.timeAdded == cachedWord.cachedWord.timeAdded)
        assertFalse(word.isFavourite)

        // check properties correct
        val prop = word.wordProperties[0]
        assertTrue(prop.definition == "$WORD_TO_CREATE")
        assertTrue(prop.partOfSpeech == "$WORD_TO_CREATE")
        assertTrue(prop.synonyms.size == WORD_TO_CREATE)
        assertTrue(prop.derivation.size == WORD_TO_CREATE)
        assertEquals(prop.examples.size, WORD_TO_CREATE)

        // check lists correct
        val index = Random.nextInt(0, WORD_TO_CREATE)
        val synonym = prop.synonyms[index]
        val derivation = prop.derivation[index]
        val example = prop.examples[index]
        assertTrue(synonym == "$index")
        assertTrue(derivation == "$index")
        assertTrue(example == "$index")
    }
}


















