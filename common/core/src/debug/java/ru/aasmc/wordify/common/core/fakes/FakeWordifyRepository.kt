package ru.aasmc.wordify.common.core.fakes

import androidx.paging.PagingData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import ru.aasmc.wordify.common.core.domain.Result
import ru.aasmc.wordify.common.core.domain.model.RecentlySearchedWord
import ru.aasmc.wordify.common.core.domain.model.Word
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import javax.inject.Inject

class FakeWordifyRepository @Inject constructor() : WordRepository {

    var success = true

    private var dummyWords = MutableList(5) {
        FakeDomainWordFactory.createDomainWord(it + 1)
    }

    fun clearWords() {
        dummyWords.clear()
    }

    private fun addFiveWords() {
        dummyWords.addAll(
            MutableList(5) {
                FakeDomainWordFactory.createDomainWord(it + 1)
            }
        )
    }

    fun refresh() {
        clearWords()
        addFiveWords()
    }

    private val recentlySearchedWords = mutableListOf<RecentlySearchedWord>()

    private fun getSortedPagingDataFlow(sort: Sort, words: List<Word>): Flow<PagingData<Word>> {
        val sorted = when (sort) {
            Sort.ASC_NAME -> words.sortedBy { it.wordName }
            Sort.DESC_NAME -> words.sortedByDescending { it.wordName }
            Sort.ASC_TIME -> words.sortedBy { it.timeAdded }
            Sort.DESC_TIME -> words.sortedByDescending { it.timeAdded }
        }
        return flowOf(PagingData.from(sorted))
    }

    override fun getAllWords(sort: Sort): Flow<PagingData<Word>> {
        return getSortedPagingDataFlow(sort, dummyWords)
    }

    override suspend fun getWordByName(wordName: String): Result<Word> {
        return if (success) {
            val word = dummyWords.first {
                it.wordName == wordName
            }
            Result.Success(word)
        } else {
            Result.Failure("No such word")
        }
    }

    override fun searchWord(word: String, sort: Sort): Flow<PagingData<Word>> {
        val words = dummyWords.filter { it.wordName.contains(word) }
        return getSortedPagingDataFlow(sort, words)
    }

    override suspend fun setFavourite(wordId: Long, isFavourite: Boolean) {
        val prev = dummyWords.first { it.wordId == wordId }
        val newWord = prev.copy(isFavourite = isFavourite)
        dummyWords.remove(prev)
        dummyWords.add(newWord)
    }

    override fun getAllFavWords(sort: Sort): Flow<PagingData<Word>> {
        val favs = dummyWords.filter { it.isFavourite }
        return getSortedPagingDataFlow(sort, favs)
    }

    override suspend fun insertRecentlySearchedWord(word: RecentlySearchedWord) {
        recentlySearchedWords.add(word)
    }

    override fun getAllRecentlySearchedWords(): Flow<List<RecentlySearchedWord>> {
        return flowOf(recentlySearchedWords)
    }

    override fun searchRecentlySearchedWords(query: String): Flow<List<RecentlySearchedWord>> {
        val queried = recentlySearchedWords.filter { it.word.contains(query) }
        return flowOf(queried)
    }
}