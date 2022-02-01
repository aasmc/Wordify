package ru.aasmc.wordify.common.core.data.cache

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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

    override fun getAllWordsByNameAsc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            wordDao.getAllWordsByNameAsc()
        }.flow
    }

    override fun getAllWordsByNameDesc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            wordDao.getAllWordsByNameDesc()
        }.flow
    }

    override fun getAllWordsByTimeAddedAsc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            wordDao.getAllWordsByTimeAddedAsc()
        }.flow
    }

    override fun getAllWordsByTimeAddedDesc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            wordDao.getAllWordsByTimeAddedDesc()
        }.flow
    }

    override fun searchWordsByNameAsc(name: String): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            wordDao.searchWordsByNameAsc(name)
        }.flow
    }

    override fun searchWordsByNameDesc(name: String): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            wordDao.searchWordsByNameDesc(name)
        }.flow
    }

    override fun searchWordsByTimeAddedAsc(name: String): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            wordDao.searchWordsByTimeAddedAsc(name)
        }.flow
    }

    override fun searchWordsByTimeAddedDesc(name: String): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            wordDao.searchWordsByTimeAddedDesc(name)
        }.flow
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

    override fun getAllFavWordsByTimeAddedDesc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            wordDao.getAllFavWordsByTimeAddedDesc()
        }.flow
    }

    override fun getAllFavWordsByNameAsc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            wordDao.getAllFavWordsByNameAsc()
        }.flow
    }

    override fun getAllFavWordsByNameDesc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            wordDao.getAllFavWordsByNameDesc()
        }.flow
    }

    override fun getAllFavWordsByTimeAddedAsc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            wordDao.getAllFavWordsByTimeAddedAsc()
        }.flow
    }
}