package ru.aasmc.wordify.common.core.api

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import ru.aasmc.wordify.common.core.data.api.WordifyApi
import ru.aasmc.wordify.common.core.utils.equalsDelta
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class WordifyApiTest {

    private val fakeServer = FakeServer()
    private lateinit var api: WordifyApi

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var retrofitBuilder: Retrofit.Builder

    @Before
    fun setup() {
        fakeServer.start()
        hiltRule.inject()

        api = retrofitBuilder
            .baseUrl(fakeServer.baseEndPoint)
            .build()
            .create(WordifyApi::class.java)
    }

    @After
    fun tearDown() {
        fakeServer.shutDown()
    }

    @Test
    fun getWord_retrieves_correct_word_with_correct_request() = runTest {
        fakeServer.setHappyPathDispatcher()
        val word = api.getWord("track")
        assertEquals("track", word.word)
        val wordFrequency = word.frequency ?: 0f
        assertTrue(4.67f.equalsDelta(wordFrequency))
        assertEquals(1, word.syllables.count)
        assertEquals("træk", word.pronunciationDto?.all)
        assertEquals(16, word.wordProperties?.size)
    }

}



















