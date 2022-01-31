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

    override fun getAllWordsByNameAsc(): Flow<List<CachedWordAggregate>> {
        return wordDao.getAllWordsByNameAsc()
    }

    override fun getAllWordsByNameDesc(): Flow<List<CachedWordAggregate>> {
        return wordDao.getAllWordsByNameDesc()
    }

    override fun getAllWordsByTimeAddedAsc(): Flow<List<CachedWordAggregate>> {
        return wordDao.getAllWordsByTimeAddedAsc()
    }

    override fun getAllWordsByTimeAddedDesc(): Flow<List<CachedWordAggregate>> {
        return wordDao.getAllWordsByTimeAddedDesc()
    }

    override fun searchWordsByNameAsc(name: String): Flow<List<CachedWordAggregate>> {
        return wordDao.searchWordsByNameAsc(name)
    }

    override fun searchWordsByNameDesc(name: String): Flow<List<CachedWordAggregate>> {
        return wordDao.searchWordsByNameDesc(name)
    }

    override fun searchWordsByTimeAddedAsc(name: String): Flow<List<CachedWordAggregate>> {
        return wordDao.searchWordsByTimeAddedAsc(name)
    }

    override fun searchWordsByTimeAddedDesc(name: String): Flow<List<CachedWordAggregate>> {
        return wordDao.searchWordsByTimeAddedDesc(name)
    }

    override suspend fun getWordById(name: String): CachedWordAggregate? {
        return wordDao.getWordById(name)
    }

    override suspend fun setFavourite(wordId: String, isFavourite: Boolean) {
        if (isFavourite) {
            wordDao.setFavourite(wordId)
        } else {
            wordDao.setNotFavourite(wordId)
        }
    }

    override fun getAllFavWordsByTimeAddedDesc(): Flow<List<CachedWordAggregate>> {
        return wordDao.getAllFavWordsByTimeAddedDesc()
    }

    override fun getAllFavWordsByNameAsc(): Flow<List<CachedWordAggregate>> {
        return wordDao.getAllFavWordsByNameAsc()
    }

    override fun getAllFavWordsByNameDesc(): Flow<List<CachedWordAggregate>> {
        return wordDao.getAllFavWordsByNameDesc()
    }

    override fun getAllFavWordsByTimeAddedAsc(): Flow<List<CachedWordAggregate>> {
        return wordDao.getAllFavWordsByTimeAddedAsc()
    }
}