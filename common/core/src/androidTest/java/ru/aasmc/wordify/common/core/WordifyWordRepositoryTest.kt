package ru.aasmc.wordify.common.core

import android.content.Context
import androidx.paging.*
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import ru.aasmc.wordify.common.core.api.FakeServer
import ru.aasmc.wordify.common.core.data.WordifyWordRepository
import ru.aasmc.wordify.common.core.data.api.WordifyApi
import ru.aasmc.wordify.common.core.data.api.model.mappers.WordDtoMapper
import ru.aasmc.wordify.common.core.data.cache.Cache
import ru.aasmc.wordify.common.core.data.cache.RoomCache
import ru.aasmc.wordify.common.core.data.cache.WordifyDatabase
import ru.aasmc.wordify.common.core.domain.Result
import ru.aasmc.wordify.common.core.domain.model.Word
import ru.aasmc.wordify.common.core.fakes.FakeCachedWordFactory
import ru.aasmc.wordify.common.core.utils.*
import javax.inject.Inject

/**
 * All attempts to test PagingData in the repository fail.
 * Tried:
 *  - AsyncPagingDataDiffer
 *  - Collecting flow of PagingData
 *  - Using extension function [collectData]
 *  - various test Dispatchers and TestScopes
 *
 * Nothing works. But when actually triggering [collectAsLazyPagingItems]
 * in the UI, everything works fine, so I'll leave the tests for
 * UI part.
 * I still can't figure out the reason why.
 */
@ExperimentalCoroutinesApi
@HiltAndroidTest
class WordifyWordRepositoryTest {
    private val fakeServer = FakeServer()
    private lateinit var api: WordifyApi
    private lateinit var cache: Cache
    private lateinit var repository: WordifyWordRepository
    private lateinit var db: WordifyDatabase
    private val dispatchersProvider = CoroutineDispatchersProvider()
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var retrofitBuilder: Retrofit.Builder

    @Inject
    lateinit var apiMapper: WordDtoMapper

    @Before
    fun setup() {
        hiltRule.inject()
        fakeServer.start()

        api = retrofitBuilder
            .baseUrl(fakeServer.baseEndPoint)
            .build()
            .create(WordifyApi::class.java)
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            WordifyDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        cache = RoomCache(
            db
        )

        repository = WordifyWordRepository(
            api = api,
            cache = cache,
            apiMapper = apiMapper,
            dispatchersProvider = dispatchersProvider
        )
    }

    @After
    fun tearDown() {
        fakeServer.shutDown()
        db.close()
    }

    @Test
    fun getWordById_success_with_correctRequest_word_savedInCache() = runTest {
        // given
        val wordId = "track"
        fakeServer.setHappyPathDispatcher(wordId)

        // when
        val result = repository.getWordByName(wordId)

        // then
        assert(result is Result.Success)
        val word = (result as Result.Success).data
        val cachedWord = cache.getWordByName(word.wordName)
        assert(cachedWord != null)
        assertEquals(word.wordId, cachedWord?.cachedWord?.wordId)
        assertEquals(word.pronunciation, cachedWord?.cachedWord?.pronunciation)
    }

    @Test
    fun getWordById_failure_with_incorrect_request() = runTest {
        // given
        val wordId = "incorrect"
        fakeServer.setHappyPathDispatcher(wordId)
        // when
        val result = repository.getWordByName(wordId)
        // then
        assert(result is Result.Failure)
    }

    @Test
    fun getWordById_success_from_cache() = runTest {
        // given
        cache.saveWord(FakeCachedWordFactory.createCachedWord(1))
        // when
        val result = repository.getWordByName("1")
        assert(result is Result.Success)
    }

    @Test
    fun searchWord_defaultSortByNameAsc_emptyList_onEmptyCache() = runTest {
        // given empty cache
        // when
        val result = repository.searchWord("track")

        val differ = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = testDispatcher,
            mainDispatcher = testDispatcher
        )
        launch(testDispatcher) {
            result.collectLatest {
                differ.submitData(it)
            }
        }
        advanceUntilIdle()
        assertTrue(differ.snapshot().isEmpty())
    }

    private fun insertSearchData(): List<Word> = runBlocking {
        fakeServer.setHappyPathDispatcher("track")
        val w = repository.getWordByName("track")
        assert(w is Result.Success)
        fakeServer.setHappyPathDispatcher("kingdom")
        val ww = repository.getWordByName("kingdom")
        assert(ww is Result.Success)
        fakeServer.setHappyPathDispatcher("make")
        val www = repository.getWordByName("kingdom")
        assert(www is Result.Success)
        return@runBlocking listOf(
            (w as Result.Success).data,
            (ww as Result.Success).data,
            (www as Result.Success).data
        )
    }

    @Test
    fun searchWord_defaultSortByNameAsc_success() = runTest {
        // given
        val words = insertSearchData()

        val result = repository.searchWord("k")
        val differ = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback(),
            updateCallback = NoopListCallback(),
            mainDispatcher = testDispatcher,
            workerDispatcher = testDispatcher
        )
        val retrieved = mutableListOf<Word>()
        val job = launch {
            result.collectLatest {
                it.map { w ->
                    retrieved.add(w)
                }
                differ.submitData(it)
            }
        }

        advanceUntilIdle()
        Truth.assertThat(retrieved).containsExactly(
            words[0],
            words[1],
            words[2],
        )
        Truth.assertThat(differ.snapshot()).containsExactly(
            words[0],
            words[1],
            words[2],
        )
        job.cancel()
    }
//
//    @Test
//    fun getAllWords_defaultSortByNameAsc_emptyList_onEmptyCache() = runTest {
//        // given empty cache
//        // when
//        val result = repository.getAllWords().take(1).single()
//        // then
//        assert(result.isEmpty())
//    }
//
//    @Test
//    fun getAllWords_defaultSortByNameAsc_success() = runTest {
//        // given
//        fakeServer.setHappyPathDispatcher("track")
//        repository.getWordById("track")
//        fakeServer.setHappyPathDispatcher("kingdom")
//        repository.getWordById("kingdom")
//
//        // when
//        val result = repository.getAllWords().take(1).single()
//        // then
//        assert(result.size == 2)
//        for(i in 0 until result.lastIndex) {
//            assert(result[i].wordId < result[i + 1].wordId)
//        }
//        assertEquals("kingdom", result[0].wordId)
//        assertEquals("track", result[1].wordId)
//    }
//
//    @Test
//    fun getAllWords_sortByNameDesc_success() = runTest {
//        // given
//        fakeServer.setHappyPathDispatcher("track")
//        repository.getWordById("track")
//        fakeServer.setHappyPathDispatcher("kingdom")
//        repository.getWordById("kingdom")
//
//        // when
//        val result = repository.getAllWords(Sort.DESC_NAME).take(1).single()
//        // then
//        assert(result.size == 2)
//        for(i in 0 until result.lastIndex) {
//            assert(result[i].wordId > result[i + 1].wordId)
//        }
//        assertEquals("kingdom", result[1].wordId)
//        assertEquals("track", result[0].wordId)
//    }

    @Test
    fun setFavourite_success() = runTest {
        fakeServer.setHappyPathDispatcher("track")
        val result = repository.getWordByName("track")
        assert(result is Result.Success)
        val word = (result as Result.Success).data
        assertFalse(word.isFavourite)

        repository.setFavourite(word.wordId, true)

        val retrievedResult = repository.getWordByName(word.wordName)
        assert(retrievedResult is Result.Success)
        val retrievedWord = (retrievedResult as Result.Success).data
        assertTrue(retrievedWord.isFavourite)
    }

    @Test
    fun setNotFavourite_success() = runTest {
        fakeServer.setHappyPathDispatcher("track")
        val result = repository.getWordByName("track")
        val word = (result as Result.Success).data
        repository.setFavourite(word.wordId, true)
        val retrievedResult = repository.getWordByName(word.wordName)
        val retrievedWord = (retrievedResult as Result.Success).data
        assertTrue(retrievedWord.isFavourite)

        repository.setFavourite(retrievedWord.wordId, false)
        val retrievedNotFavResult = repository.getWordByName(retrievedWord.wordName)
        assertTrue(retrievedNotFavResult is Result.Success)
        val notFavWord = (retrievedNotFavResult as Result.Success).data
        assertFalse(notFavWord.isFavourite)
    }

//    @Test
//    fun getAllFavWords_defaultSortByNameAsc_success() = runTest {
//        // given
//        fakeServer.setHappyPathDispatcher("track")
//        repository.getWordById("track")
//        fakeServer.setHappyPathDispatcher("kingdom")
//        repository.getWordById("kingdom")
//        repository.setFavourite("track", true)
//
//        val favWords = repository.getAllFavWords().take(1).single()
//        assertTrue(favWords.size == 1)
//        assertTrue(favWords[0].wordId == "track")
//    }

}

















