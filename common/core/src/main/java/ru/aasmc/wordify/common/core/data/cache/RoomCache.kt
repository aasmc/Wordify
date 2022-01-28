package ru.aasmc.wordify.common.core.data.cache

import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.data.cache.dao.WordDao
import ru.aasmc.wordify.common.core.data.cache.model.CachedWordAggregate
import javax.inject.Inject

class RoomCache @Inject constructor(
    private val wordDao: WordDao
) : Cache {
    override suspend fun saveWord(cachedWord: CachedWordAggregate) {
        wordDao.insertCachedWordAggregate(cachedWord)
    }

    override fun getAllWords(): Flow<List<CachedWordAggregate>> {
        return wordDao.getAllWords()
    }

    override fun searchWord(name: String): Flow<List<CachedWordAggregate>> {
        return wordDao.searchWordsByName(name)
    }
}