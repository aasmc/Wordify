package ru.aasmc.wordify.common.core.data.cache

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.data.cache.model.CachedWordAggregate

interface Cache {

    suspend fun saveWord(cachedWord: CachedWordAggregate)

    fun getAllWordsByNameAsc(): Flow<PagingData<CachedWordAggregate>>

    fun getAllWordsByNameDesc(): Flow<PagingData<CachedWordAggregate>>

    fun getAllWordsByTimeAddedAsc(): Flow<PagingData<CachedWordAggregate>>

    fun getAllWordsByTimeAddedDesc(): Flow<PagingData<CachedWordAggregate>>

    fun searchWordsByNameAsc(name: String): Flow<PagingData<CachedWordAggregate>>

    fun searchWordsByNameDesc(name: String): Flow<PagingData<CachedWordAggregate>>

    fun searchWordsByTimeAddedAsc(name: String): Flow<PagingData<CachedWordAggregate>>

    fun searchWordsByTimeAddedDesc(name: String): Flow<PagingData<CachedWordAggregate>>

    suspend fun getWordByName(wordName: String): CachedWordAggregate?

    suspend fun setFavourite(wordId: Long, isFavourite: Boolean)

    fun getAllFavWordsByTimeAddedDesc(): Flow<PagingData<CachedWordAggregate>>

    fun getAllFavWordsByNameAsc(): Flow<PagingData<CachedWordAggregate>>

    fun getAllFavWordsByNameDesc(): Flow<PagingData<CachedWordAggregate>>

    fun getAllFavWordsByTimeAddedAsc(): Flow<PagingData<CachedWordAggregate>>

}