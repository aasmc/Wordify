package ru.aasmc.wordify.common.core.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.domain.Result
import ru.aasmc.wordify.common.core.domain.model.Word

interface WordRepository {

    fun getAllWords(): Flow<List<Word>>

    suspend fun getWordById(wordId: String): Result<Word>

    fun searchWord(word: String): Flow<List<Word>>

}