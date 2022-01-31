package ru.aasmc.wordify.common.core.data.cache

import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.data.cache.model.CachedWordAggregate

interface Cache {

    suspend fun saveWord(cachedWord: CachedWordAggregate)

    fun getAllWordsByNameAsc(): Flow<List<CachedWordAggregate>>

    fun getAllWordsByNameDesc(): Flow<List<CachedWordAggregate>>

    fun getAllWordsByTimeAddedAsc(): Flow<List<CachedWordAggregate>>

    fun getAllWordsByTimeAddedDesc(): Flow<List<CachedWordAggregate>>

    fun searchWordsByNameAsc(name: String): Flow<List<CachedWordAggregate>>

    fun searchWordsByNameDesc(name: String): Flow<List<CachedWordAggregate>>

    fun searchWordsByTimeAddedAsc(name: String): Flow<List<CachedWordAggregate>>

    fun searchWordsByTimeAddedDesc(name: String): Flow<List<CachedWordAggregate>>

    suspend fun getWordById(name: String): CachedWordAggregate?

    suspend fun setFavourite(wordId: String, isFavourite: Boolean)

    fun getAllFavWordsByTimeAddedDesc(): Flow<List<CachedWordAggregate>>

    fun getAllFavWordsByNameAsc(): Flow<List<CachedWordAggregate>>

    fun getAllFavWordsByNameDesc(): Flow<List<CachedWordAggregate>>

    fun getAllFavWordsByTimeAddedAsc(): Flow<List<CachedWordAggregate>>

}