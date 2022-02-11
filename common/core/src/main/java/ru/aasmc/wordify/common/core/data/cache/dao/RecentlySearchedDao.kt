package ru.aasmc.wordify.common.core.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.data.cache.model.CachedRecentlySearchedWord

@Dao
interface RecentlySearchedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: CachedRecentlySearchedWord)

    @Query("SELECT * from recently_searched_words ORDER BY time_added DESC")
    fun getRecentlySearchedWords(): Flow<List<CachedRecentlySearchedWord>>

    @Query("SELECT * FROM recently_searched_words WHERE word LIKE '%' || :query || '%' ORDER BY time_added")
    fun searchRecentlySearchedWords(query: String): Flow<List<CachedRecentlySearchedWord>>
}