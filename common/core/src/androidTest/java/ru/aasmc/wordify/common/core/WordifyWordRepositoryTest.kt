package ru.aasmc.wordify.common.core

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
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
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import ru.aasmc.wordify.common.core.fakes.FakeCachedWordFactory
import ru.aasmc.wordify.common.core.utils.CoroutineDispatchersProvider
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class WordifyWordRepositoryTest {
    private val fakeServer = FakeServer()
    private lateinit var api: WordifyApi
    private lateinit var cache: Cache
    private lateinit var repository: WordRepository
    private lateinit var db: WordifyDatabase
    private val dispatchersProvider = CoroutineDispatchersProvider()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var retrofitBuilder: Retrofit.Builder


    @Inject
    lateinit var apiMapper: WordDtoMapper


    @Before
    fun setup() {
        fakeServer.start()
        hiltRule.inject()

        api = retrofitBuilder
            .baseUrl(fakeServer.baseEndPoint)
            .build()
            .create(WordifyApi::class.java)
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            WordifyDatabase::class.java
        )
            .build()
        cache = RoomCache(
            db.wordDao()
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
        val result = repository.getWordById(wordId)

        // then
        assert(result is Result.Success)
        val word = (result as Result.Success).data
        val cachedWord = cache.getWordById(word.name)
        assert(cachedWord != null)
        assertEquals(word.name, cachedWord?.cachedWord?.wordId)
        assertEquals(word.pronunciation, cachedWord?.cachedWord?.pronunciation)
    }

    @Test
    fun getWordById_failure_with_incorrect_request() = runTest {
        // given
        val wordId = "incorrect"
        fakeServer.setHappyPathDispatcher(wordId)
        // when
        val result = repository.getWordById(wordId)
        // then
        assert(result is Result.Failure)
    }

    @Test
    fun getWordById_success_from_cache() = runTest {
        // given
        cache.saveWord(FakeCachedWordFactory.createCachedWord(1))
        // when
        val result = repository.getWordById("1")
        assert(result is Result.Success)
    }

    @Test
    fun searchWord_emptyList_onEmptyCache() = runTest {
        // given empty cache
        // when
        val result = repository.searchWord("track").take(1).single()
        assert(result.isEmpty())
    }

    @Test
    fun searchWord_success() = runTest {
        // given
        fakeServer.setHappyPathDispatcher("track")
        repository.getWordById("track")
        fakeServer.setHappyPathDispatcher("kingdom")
        repository.getWordById("kingdom")

        // when
        val result = repository.searchWord("k").take(1).single()
        // then
        assertEquals(2, result.size)
        for (i in 0 until result.lastIndex) {
            assert(result[i].name < result[i + 1].name)
        }
    }

    @Test
    fun getAllWords_emptyList_onEmptyCache() = runTest {
        // given empty cache
        // when
        val result = repository.getAllWords().take(1).single()
        // then
        assert(result.isEmpty())
    }

    @Test
    fun getAllWords_success() = runTest {
        // given
        fakeServer.setHappyPathDispatcher("track")
        repository.getWordById("track")
        fakeServer.setHappyPathDispatcher("kingdom")
        repository.getWordById("kingdom")

        // when
        val result = repository.getAllWords().take(1).single()
        // then
        assert(result.size == 2)
        for(i in 0 until result.lastIndex) {
            assert(result[i].name < result[i + 1].name)
        }
        assertEquals("kingdom", result[0].name)
        assertEquals("track", result[1].name)
    }

}

















