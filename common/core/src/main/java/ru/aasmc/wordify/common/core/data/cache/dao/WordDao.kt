package ru.aasmc.wordify.common.core.data.cache.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.data.cache.model.*

@Dao
abstract class WordDao {

    @Transaction
    @Query("SELECT * FROM words ORDER BY wordId ASC")
    abstract fun getAllWordsByNameAsc(): Flow<List<CachedWordAggregate>>

    @Transaction
    @Query("SELECT * FROM words ORDER BY wordId DESC")
    abstract fun getAllWordsByNameDesc(): Flow<List<CachedWordAggregate>>

    @Transaction
    @Query("SELECT * FROM words ORDER BY timeAdded ASC")
    abstract fun getAllWordsByTimeAddedAsc(): Flow<List<CachedWordAggregate>>

    @Transaction
    @Query("SELECT * FROM words ORDER BY timeAdded DESC")
    abstract fun getAllWordsByTimeAddedDesc(): Flow<List<CachedWordAggregate>>

    @Transaction
    @Query("SELECT * FROM words WHERE isFavourite = 1 ORDER BY wordId ASC")
    abstract fun getAllFavWordsByNameAsc(): Flow<List<CachedWordAggregate>>

    @Transaction
    @Query("SELECT * FROM words WHERE isFavourite = 1 ORDER BY wordId DESC")
    abstract fun getAllFavWordsByNameDesc(): Flow<List<CachedWordAggregate>>

    @Transaction
    @Query("SELECT * FROM words WHERE isFavourite = 1 ORDER BY timeAdded ASC")
    abstract fun getAllFavWordsByTimeAddedAsc(): Flow<List<CachedWordAggregate>>

    @Transaction
    @Query("SELECT * FROM words WHERE isFavourite = 1 ORDER BY timeAdded DESC")
    abstract fun getAllFavWordsByTimeAddedDesc(): Flow<List<CachedWordAggregate>>

    @Transaction
    @Query("SELECT * FROM words WHERE wordId = :wordId")
    abstract suspend fun getWordById(wordId: String): CachedWordAggregate?

    @Query("UPDATE words SET isFavourite = 1 WHERE wordId =:word")
    abstract suspend fun setFavourite(word: String)

    @Query("UPDATE words SET isFavourite = 0 WHERE wordId =:word")
    abstract suspend fun setNotFavourite(word: String)

    /**
     * Can't insert CachedWordPropertiesAggregate, since it is not an Entity,
     * but can decompose it into its @Entity annotated components and pass
     * them into this method. Room will know how to insert them.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWordPropertiesAggregate(
        cachedWordProperties: CachedWordProperties,
        synonyms: List<CachedSynonym>,
        derivations: List<CachedDerivation>,
        examples: List<CachedExample>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertCachedWord(
        cachedWord: CachedWord
    )

    suspend fun insertCachedWordAggregate(cachedWordAggregate: CachedWordAggregate) {
        insertCachedWord(cachedWordAggregate.cachedWord)
        for (prop in cachedWordAggregate.wordProperties) {
            insertWordPropertiesAggregate(
                cachedWordProperties = prop.cachedWordProperties,
                synonyms = prop.synonyms,
                derivations = prop.derivations,
                examples = prop.examples
            )
        }
    }

    @Transaction
    @Query("SELECT * FROM words WHERE wordId LIKE '%' || :name || '%' ORDER BY wordId ASC")
    abstract fun searchWordsByNameAsc(name: String): Flow<List<CachedWordAggregate>>

    @Transaction
    @Query("SELECT * FROM words WHERE wordId LIKE '%' || :name || '%' ORDER BY wordId DESC")
    abstract fun searchWordsByNameDesc(name: String): Flow<List<CachedWordAggregate>>

    @Transaction
    @Query("SELECT * FROM words WHERE wordId LIKE '%' || :name || '%' ORDER BY timeAdded DESC")
    abstract fun searchWordsByTimeAddedDesc(name: String): Flow<List<CachedWordAggregate>>

    @Transaction
    @Query("SELECT * FROM words WHERE wordId LIKE '%' || :name || '%' ORDER BY timeAdded ASC")
    abstract fun searchWordsByTimeAddedAsc(name: String): Flow<List<CachedWordAggregate>>

}