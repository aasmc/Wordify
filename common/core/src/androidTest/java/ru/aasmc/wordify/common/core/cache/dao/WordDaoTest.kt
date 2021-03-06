package ru.aasmc.wordify.common.core.cache.dao

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.aasmc.wordify.common.core.data.cache.WordifyDatabase
import ru.aasmc.wordify.common.core.data.cache.dao.WordDao
import ru.aasmc.wordify.common.core.data.cache.model.CachedWordAggregate
import ru.aasmc.wordify.common.core.fakes.FakeCachedWordFactory

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class WordDaoTest {
    private lateinit var wordifyDatabase: WordifyDatabase
    private lateinit var wordDao: WordDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        wordifyDatabase = Room.inMemoryDatabaseBuilder(
            context,
            WordifyDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        wordDao = wordifyDatabase.wordDao()
    }

    @After
    fun closeDatabase() {
        wordifyDatabase.close()
    }

    private fun createPagerData(size: Int, source: () -> PagingSource<Int, CachedWordAggregate>):
            Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = size,

            ),
        ) {
            source()
        }.flow
    }

    @Test
    fun insertCachedWordAggregate_getByWordId_correct() = runTest {
        // given
        val cachedWord = FakeCachedWordFactory.createCachedWord(2)
        // when
        wordDao.insertCachedWordAggregate(cachedWord)
        val retrieved = wordDao.getWordByName(cachedWord.cachedWord.wordName)

        // then

        assertEquals(cachedWord.cachedWord.wordId, retrieved?.cachedWord?.wordId)
        assertEquals(cachedWord.cachedWord.pronunciation, retrieved?.cachedWord?.pronunciation)
        assertEquals(cachedWord.cachedWord.frequency, retrieved?.cachedWord?.frequency)
        assertEquals(cachedWord.cachedWord.syllable, retrieved?.cachedWord?.syllable)
        assertFalse(retrieved?.cachedWord?.isFavourite ?: throw IllegalStateException("Cached word in test should not be null"))
        assertEquals(cachedWord.wordProperties.size, retrieved.wordProperties.size)
        val expectedProps = cachedWord.wordProperties[0]
        val retrievedProps = retrieved.wordProperties.find {
            it.cachedWordProperties.propertiesId == expectedProps.cachedWordProperties.propertiesId
        } ?: throw IllegalStateException("Properties in the test should be found")

        assertEquals(expectedProps.derivations.size, retrievedProps.derivations.size)
        val expectedDerivation = expectedProps.derivations[0]
        val retrievedDerivation = retrievedProps.derivations.find {
            it.derivationId == expectedDerivation.derivationId
        } ?: throw IllegalStateException("Derivation should be found in the test.")
        assertEquals(
            expectedDerivation.derivation,
            retrievedDerivation.derivation
        )

        assertEquals(expectedProps.examples.size, retrievedProps.examples.size)
        val expectedExamples = expectedProps.examples[0]
        val retrievedExamples = retrievedProps.examples.find {
            it.exampleId == expectedExamples.exampleId
        } ?: throw IllegalStateException("Examples should be found in the test.")
        assertEquals(
            expectedExamples.example,
            retrievedExamples.example
        )

        assertEquals(expectedProps.synonyms.size, retrievedProps.synonyms.size)
        val expectedSynonyms = expectedProps.synonyms[0]
        val retrievedSynonyms = retrievedProps.synonyms.find {
            it.synonymId == expectedSynonyms.synonymId
        } ?: throw IllegalStateException("Synonyms should be found in the test.")
        assertEquals(
            expectedSynonyms.synonym,
            retrievedSynonyms.synonym
        )

        assertEquals(
            expectedProps.cachedWordProperties.wordId,
            retrievedProps.cachedWordProperties.wordId
        )
        assertEquals(
            expectedProps.cachedWordProperties.definition,
            retrievedProps.cachedWordProperties.definition
        )
        assertEquals(
            expectedProps.cachedWordProperties.partOfSpeech,
            retrievedProps.cachedWordProperties.partOfSpeech
        )
    }

    @Test
    fun getWordById_success_favourite_word() = runTest {
        // given
        val cachedWord = FakeCachedWordFactory.createFavCacheWord(1)
        wordDao.insertCachedWordAggregate(cachedWord)
        // when
        val retrieved = wordDao.getWordByName(cachedWord.cachedWord.wordName)
        // then
        assertTrue(
            retrieved?.cachedWord?.isFavourite ?: throw Exception("Word in the test cannot be null")
        )
    }

    @Test
    fun getAllWordsByNameAsc_correctly_returns_flowWithListOf_10_Words_after_inserting_10_Words() =
        runTest {
            // given
            (1..10).forEach { wordId ->
                wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(wordId))
            }
            // when
            val source = wordDao.getAllWordsByNameAsc()

            val actual = source.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 5,
                    placeholdersEnabled = false
                )
            )

            assertTrue(actual is PagingSource.LoadResult.Page)

            val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

            Truth.assertThat(words.size).isEqualTo(5)
            for (i in 0 until words.lastIndex) {
                Truth.assertThat(words[i].cachedWord.wordName)
                    .isLessThan(words[i + 1].cachedWord.wordName)
            }
        }

    @Test
    fun getAllWordsByNameDesc_success() = runTest {
        // given
        (1..10).forEach { wordId ->
            wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(wordId))
        }

        // when
        val source = wordDao.getAllWordsByNameDesc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(5)
        for (i in 0 until words.lastIndex) {
            Truth.assertThat(words[i].cachedWord.wordName)
                .isGreaterThan(words[i + 1].cachedWord.wordName)
        }
    }

    @Test
    fun getAllWordsByTimeAddedDesc_success() = runTest {
        // given
        (1..10).forEach { wordId ->
            wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(wordId))
        }

        // when
        val source = wordDao.getAllWordsByTimeAddedDesc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(5)
        for (i in 0 until words.lastIndex) {
            Truth.assertThat(words[i].cachedWord.timeAdded)
                .isGreaterThan(words[i + 1].cachedWord.timeAdded)
        }
    }

    @Test
    fun getAllWordsByTimeAddedAsc_success() = runTest {
        // given
        (1..10).forEach { wordId ->
            wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(wordId))
        }

        // when
        val source = wordDao.getAllWordsByTimeAddedAsc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(5)
        for (i in 0 until words.lastIndex) {
            Truth.assertThat(words[i].cachedWord.wordName)
                .isLessThan(words[i + 1].cachedWord.wordName)
        }
    }

    @Test
    fun getAllWordsByNameAsc_correctSize() = runTest {
        // given
        (1..10).forEach { wordId ->
            wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(wordId))
        }
        // when
        val source = wordDao.getAllWordsByNameAsc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(10)
        for (i in 0 until words.lastIndex) {
            Truth.assertThat(words[i].cachedWord.wordName)
                .isLessThan(words[i + 1].cachedWord.wordName)
        }
    }

    @Test
    fun getAllWordsByNameAsc_emptyList() = runTest {
        // when
        val source = wordDao.getAllWordsByNameAsc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(0)
    }

    @Test
    fun getAllWordsByNameDesc_emptyList() = runTest {
        // when
        val source = wordDao.getAllWordsByNameDesc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(0)
    }

    @Test
    fun getAllWordsByTimeAddedAsc_emptyList() = runTest {
        // when
        val source = wordDao.getAllWordsByTimeAddedAsc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(0)
    }

    @Test
    fun getAllWordsByTimeAddedDesc_emptyList() = runTest {
        // when
        val source = wordDao.getAllWordsByTimeAddedDesc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(0)
    }

    @Test
    fun getAllFavWordsByTimeAddedAsc_success() = runTest {
        // given
        (1..10).forEach { wordId ->
            wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createFavCacheWord(wordId))
        }

        // when
        val source = wordDao.getAllFavWordsByTimeAddedAsc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(5)
        for (i in 0 until words.lastIndex) {
            val prev = words[i].cachedWord
            val next = words[i + 1].cachedWord
            Truth.assertThat(prev.timeAdded)
                .isLessThan(next.timeAdded)
            assertTrue(prev.isFavourite)
            assertTrue(next.isFavourite)
        }
    }

    @Test
    fun getAllFavWordsByTimeAddedDesc_success() = runTest {
        // given
        (1..10).forEach { wordId ->
            wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createFavCacheWord(wordId))
        }

        // when
        val source = wordDao.getAllFavWordsByTimeAddedDesc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(5)
        for (i in 0 until words.lastIndex) {
            val prev = words[i].cachedWord
            val next = words[i + 1].cachedWord
            Truth.assertThat(prev.timeAdded)
                .isGreaterThan(next.timeAdded)
            assertTrue(prev.isFavourite)
            assertTrue(next.isFavourite)
        }
    }

    @Test
    fun getAllFavWordsByNameDesc_success() = runTest {
        // given
        (1..10).forEach { wordId ->
            wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createFavCacheWord(wordId))
        }

        // when
        val source = wordDao.getAllFavWordsByNameDesc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(5)
        for (i in 0 until words.lastIndex) {
            val prev = words[i].cachedWord
            val next = words[i + 1].cachedWord
            Truth.assertThat(prev.wordId)
                .isGreaterThan(next.wordId)
            assertTrue(prev.isFavourite)
            assertTrue(next.isFavourite)
        }
    }

    @Test
    fun getAllFavWordsByNameAsc_success() = runTest {
        // given
        (1..10).forEach { wordId ->
            wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createFavCacheWord(wordId))
        }

        // when
        val source = wordDao.getAllFavWordsByNameAsc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(5)
        for (i in 0 until words.lastIndex) {
            val prev = words[i].cachedWord
            val next = words[i + 1].cachedWord
            Truth.assertThat(prev.wordId)
                .isLessThan(next.wordId)
            assertTrue(prev.isFavourite)
            assertTrue(next.isFavourite)
        }
    }

    @Test
    fun getAllFavWordsByNameAsc_emptyList() = runTest {
        // when
        val source = wordDao.getAllFavWordsByNameAsc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(0)
    }

    @Test
    fun getAllFavWordsByNameDesc_emptyList() = runTest {
        // when
        val source = wordDao.getAllFavWordsByNameDesc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(0)
    }

    @Test
    fun getAllFavWordsByTimeAddedAsc_emptyList() = runTest {
        // when
        val source = wordDao.getAllFavWordsByTimeAddedAsc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(0)
    }

    @Test
    fun getAllFavWordsByTimeAddedDesc_emptyList() = runTest {
        // when
        val source = wordDao.getAllFavWordsByTimeAddedDesc()

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(0)
    }

    @Test
    fun searchWordByNameAsc_returns_3_words_sorted_by_name_with_similar_names() = runTest {
        // given
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(1))
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(11))
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(111))

        (22..29).forEach {
            wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(it))
        }

        // when
        val source = wordDao.searchWordsByNameAsc("1")

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(3)
        for (i in 0 until words.lastIndex) {
            val prev = words[i].cachedWord
            val next = words[i + 1].cachedWord
            Truth.assertThat(prev.wordName)
                .isLessThan(next.wordName)
            Truth.assertThat(prev.wordName).contains("1")
            Truth.assertThat(next.wordName).contains("1")
        }
    }

    @Test
    fun searchWordByNameDesc_success() = runTest {
        // given
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(1))
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(11))
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(111))

        (22..29).forEach {
            wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(it))
        }

        // when
        val source = wordDao.searchWordsByNameDesc("1")

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(3)
        for (i in 0 until words.lastIndex) {
            val prev = words[i].cachedWord
            val next = words[i + 1].cachedWord
            Truth.assertThat(prev.wordName)
                .isGreaterThan(next.wordName)
            Truth.assertThat(prev.wordName).contains("1")
            Truth.assertThat(next.wordName).contains("1")
        }
    }

    @Test
    fun searchWordByTimeAddedDesc_success() = runTest {
        // given
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(1))
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(11))
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(111))

        (22..29).forEach {
            wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(it))
        }

        // when
        val source = wordDao.searchWordsByTimeAddedDesc("1")

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(3)
        for (i in 0 until words.lastIndex) {
            val prev = words[i].cachedWord
            val next = words[i + 1].cachedWord
            Truth.assertThat(prev.timeAdded)
                .isGreaterThan(next.timeAdded)
            Truth.assertThat(prev.wordName).contains("1")
            Truth.assertThat(next.wordName).contains("1")
        }
    }

    @Test
    fun searchWordByTimeAddedAsc_success() = runTest {
        // given
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(1))
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(11))
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(111))

        (22..29).forEach {
            wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(it))
        }

        // when
        val source = wordDao.searchWordsByTimeAddedAsc("1")

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(3)
        for (i in 0 until words.lastIndex) {
            val prev = words[i].cachedWord
            val next = words[i + 1].cachedWord
            Truth.assertThat(prev.timeAdded)
                .isLessThan(next.timeAdded)
            Truth.assertThat(prev.wordName).contains("1")
            Truth.assertThat(next.wordName).contains("1")
        }
    }


    @Test
    fun getWordById_returns_null_if_no_word_with_name_inDb() = runTest {
        // given
        val wordToSearch = "word"
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(1))
        // when
        val word = wordDao.getWordByName(wordToSearch)
        // then
        assertNull(word)
    }

    @Test
    fun searchWordsByNameAsc_returns_emptyList() = runTest {
        // given
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(1))

        // when
        val source = wordDao.searchWordsByNameAsc("track")

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(0)
    }

    @Test
    fun searchWordsByNameDesc_returns_emptyList() = runTest {
        // given
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(1))
        // when
        val source = wordDao.searchWordsByNameDesc("track")

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(0)
    }

    @Test
    fun searchWordsByTimeAddedAsc_returns_emptyList() = runTest {
        // given
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(1))
        // when
        val source = wordDao.searchWordsByTimeAddedAsc("track")

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(0)
    }

    @Test
    fun searchWordsByTimeAddedDesc_returns_emptyList() = runTest {
        // given
        wordDao.insertCachedWordAggregate(FakeCachedWordFactory.createCachedWord(1))
        // when
        val source = wordDao.searchWordsByTimeAddedDesc("track")

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(actual is PagingSource.LoadResult.Page)

        val words = (actual as? PagingSource.LoadResult.Page)?.data ?: throw Exception("Words must not be null")

        Truth.assertThat(words.size).isEqualTo(0)
    }

    @Test
    fun setFavourite_success() = runTest {
        // given
        val word = FakeCachedWordFactory.createCachedWord(1)
        wordDao.insertCachedWordAggregate(word)
        // when
        wordDao.setFavourite(word.cachedWord.wordId)
        // then
        val retrieved = wordDao.getWordByName(word.cachedWord.wordName)
        assertTrue(
            retrieved?.cachedWord?.isFavourite ?: throw Exception("Word in test cannot be null")
        )
    }

    @Test
    fun setNotFavourite_success() = runTest {
        // given
        val word = FakeCachedWordFactory.createCachedWord(1)
        wordDao.insertCachedWordAggregate(word)
        // when
        wordDao.setNotFavourite(word.cachedWord.wordId)
        // then
        val retrieved = wordDao.getWordByName(word.cachedWord.wordName)
        assertFalse(
            retrieved?.cachedWord?.isFavourite ?: throw Exception("Word in test cannot be null")
        )
    }
}


































