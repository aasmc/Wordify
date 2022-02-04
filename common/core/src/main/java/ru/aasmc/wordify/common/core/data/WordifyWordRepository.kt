package ru.aasmc.wordify.common.core.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.aasmc.wordify.common.core.data.api.WordifyApi
import ru.aasmc.wordify.common.core.data.api.model.mappers.WordDtoMapper
import ru.aasmc.wordify.common.core.data.cache.Cache
import ru.aasmc.wordify.common.core.data.cache.model.CachedWordAggregate
import ru.aasmc.wordify.common.core.domain.Result
import ru.aasmc.wordify.common.core.domain.model.Word
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import ru.aasmc.wordify.common.core.utils.DispatchersProvider
import ru.aasmc.wordify.common.core.utils.safeApiCall
import ru.aasmc.wordify.common.core.utils.safeCacheCall
import javax.inject.Inject

class WordifyWordRepository @Inject constructor(
    private val api: WordifyApi,
    private val cache: Cache,
    private val apiMapper: WordDtoMapper,
    private val dispatchersProvider: DispatchersProvider
) : WordRepository {

    override fun getAllWords(sort: Sort): Flow<List<Word>> {
        val searchFlow = when (sort) {
            Sort.ASC_NAME -> cache.getAllWordsByNameAsc()
            Sort.DESC_NAME -> cache.getAllWordsByNameDesc()
            Sort.ASC_TIME -> cache.getAllWordsByTimeAddedAsc()
            Sort.DESC_TIME -> cache.getAllWordsByTimeAddedDesc()
        }
        return searchFlow
            .distinctUntilChanged()
            .map { cachedWords ->
                cachedWords.map { cachedWord ->
                    CachedWordAggregate.toDomain(cachedWord)
                }
            }
    }

    /**
     * Tries to load a word by id from cache. If cache has no such word,
     * goes to the network and tries there. If success -> saves the word to cache
     * and returns this word from cache. In other exceptional cases, returns Result.Failure.
     */
    override suspend fun getWordById(wordId: String): Result<Word> {
        val cacheResult = safeCacheCall(dispatchersProvider.io()) {
            cache.getWordById(wordId)
        }
        if (cacheResult is Result.Success) {
            return Result.Success(data = CachedWordAggregate.toDomain(cacheResult.data))
        } else {
            val dtoResult = safeApiCall(dispatchersProvider.io()) {
                api.getWord(wordId)
            }
            when (dtoResult) {
                is Result.Failure -> {
                    return dtoResult
                }
                is Result.Success -> {
                    cache.saveWord(apiMapper.mapToCache(dtoResult.data))
                }
            }
            val cacheSecondAttempt = safeCacheCall(dispatchersProvider.io()) {
                cache.getWordById(wordId)
            }
            return when (cacheSecondAttempt) {
                is Result.Failure -> {
                    cacheSecondAttempt
                }
                is Result.Success -> {
                    Result.Success(CachedWordAggregate.toDomain(cacheSecondAttempt.data))
                }
            }
        }
    }

    override fun searchWord(word: String, sort: Sort): Flow<List<Word>> {
        val searchFlow = when (sort) {
            Sort.ASC_NAME -> cache.searchWordsByNameAsc(word)
            Sort.DESC_NAME -> cache.searchWordsByNameDesc(word)
            Sort.ASC_TIME -> cache.searchWordsByTimeAddedAsc(word)
            Sort.DESC_TIME -> cache.searchWordsByTimeAddedDesc(word)
        }
        return searchFlow
            .distinctUntilChanged()
            .map { cachedWords ->
                cachedWords.map { cachedWord ->
                    CachedWordAggregate.toDomain(cachedWord)
                }
            }
    }

    override suspend fun setFavourite(wordId: String, isFavourite: Boolean) {
        cache.setFavourite(wordId, isFavourite)
    }

    override fun getAllFavWords(sort: Sort): Flow<List<Word>> {
        val wordsFlow = when (sort) {
            Sort.ASC_NAME -> cache.getAllFavWordsByNameAsc()
            Sort.DESC_NAME -> cache.getAllFavWordsByNameDesc()
            Sort.ASC_TIME -> cache.getAllFavWordsByTimeAddedAsc()
            Sort.DESC_TIME -> cache.getAllFavWordsByTimeAddedDesc()
        }
        return wordsFlow
            .distinctUntilChanged()
            .map { cachedWords ->
                cachedWords.map { cachedWord ->
                    CachedWordAggregate.toDomain(cachedWord)
                }
            }
    }
}











