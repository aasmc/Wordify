package ru.aasmc.wordify.common.core.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.domain.Result
import ru.aasmc.wordify.common.core.domain.model.Word

interface WordRepository {

    fun getAllWords(sort: Sort = Sort.ASC_NAME): Flow<List<Word>>

    suspend fun getWordById(wordId: String): Result<Word>

    fun searchWord(word: String, sort: Sort = Sort.ASC_NAME): Flow<List<Word>>

    suspend fun setFavourite(wordId: String, isFavourite: Boolean)

    fun getAllFavWords(sort: Sort = Sort.ASC_NAME): Flow<List<Word>>

}