package ru.aasmc.wordify.common.core.data.cache

import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.data.cache.model.CachedWordAggregate

interface Cache {

    suspend fun saveWord(cachedWord: CachedWordAggregate)

    fun getAllWords(): Flow<List<CachedWordAggregate>>

    fun searchWord(name: String): Flow<List<CachedWordAggregate>>

}