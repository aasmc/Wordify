package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.aasmc.wordify.common.core.domain.model.RecentlySearchedWord

@Entity(tableName = "recently_searched_words")
data class CachedRecentlySearchedWord(
    @PrimaryKey
    @ColumnInfo(name = "word")
    val word: String,
    @ColumnInfo(name = "time_added")
    val timeAdded: Long
) {
    companion object {
        fun toDomain(cachedWord: CachedRecentlySearchedWord): RecentlySearchedWord {
            return RecentlySearchedWord(
                word = cachedWord.word,
                timeAdded = cachedWord.timeAdded
            )
        }

        fun fromDomain(word: RecentlySearchedWord): CachedRecentlySearchedWord {
            return CachedRecentlySearchedWord(
                word = word.word,
                timeAdded = word.timeAdded
            )
        }
    }
}
