package ru.aasmc.wordify.common.core.data.cache

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import ru.aasmc.constants.CacheConstants.MAX_SIZE
import ru.aasmc.constants.CacheConstants.PAGE_SIZE
import ru.aasmc.wordify.common.core.data.cache.model.CachedWordAggregate
import javax.inject.Inject

class RoomCache @Inject constructor(
    private val db: WordifyDatabase
) : Cache {
    private val wordDao = db.wordDao()
    override suspend fun saveWord(cachedWord: CachedWordAggregate) {
        db.withTransaction {
            wordDao.insertCachedWordAggregate(cachedWord)
        }
    }

    override fun getAllWordsByNameAsc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = MAX_SIZE
            )
        ) {
            wordDao.getAllWordsByNameAsc()
        }.flow
    }

    override fun getAllWordsByNameDesc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = MAX_SIZE
            )
        ) {
            wordDao.getAllWordsByNameDesc()
        }.flow
    }

    override fun getAllWordsByTimeAddedAsc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = MAX_SIZE
            )
        ) {
            wordDao.getAllWordsByTimeAddedAsc()
        }.flow
    }

    override fun getAllWordsByTimeAddedDesc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = MAX_SIZE
            )
        ) {
            wordDao.getAllWordsByTimeAddedDesc()
        }.flow
    }

    override fun searchWordsByNameAsc(name: String): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = MAX_SIZE
            )
        ) {
            wordDao.searchWordsByNameAsc(name)
        }.flow
    }

    override fun searchWordsByNameDesc(name: String): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = MAX_SIZE
            )
        ) {
            wordDao.searchWordsByNameDesc(name)
        }.flow
    }

    override fun searchWordsByTimeAddedAsc(name: String): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = MAX_SIZE
            )
        ) {
            wordDao.searchWordsByTimeAddedAsc(name)
        }.flow
    }

    override fun searchWordsByTimeAddedDesc(name: String): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = MAX_SIZE
            )
        ) {
            wordDao.searchWordsByTimeAddedDesc(name)
        }.flow
    }

    override suspend fun getWordByName(wordName: String ): CachedWordAggregate? {
        return wordDao.getWordByName(wordName)
    }

    override suspend fun setFavourite(wordId: Long, isFavourite: Boolean) {
        if (isFavourite) {
            wordDao.setFavourite(wordId)
        } else {
            wordDao.setNotFavourite(wordId)
        }
    }

    override fun getAllFavWordsByTimeAddedDesc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = MAX_SIZE
            )
        ) {
            wordDao.getAllFavWordsByTimeAddedDesc()
        }.flow
    }

    override fun getAllFavWordsByNameAsc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = MAX_SIZE
            )
        ) {
            wordDao.getAllFavWordsByNameAsc()
        }.flow
    }

    override fun getAllFavWordsByNameDesc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = MAX_SIZE
            )
        ) {
            wordDao.getAllFavWordsByNameDesc()
        }.flow
    }

    override fun getAllFavWordsByTimeAddedAsc(): Flow<PagingData<CachedWordAggregate>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = MAX_SIZE
            )
        ) {
            wordDao.getAllFavWordsByTimeAddedAsc()
        }.flow
    }
}