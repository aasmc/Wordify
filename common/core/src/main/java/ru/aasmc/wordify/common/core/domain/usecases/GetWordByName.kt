package ru.aasmc.wordify.common.core.domain.usecases

import ru.aasmc.wordify.common.core.domain.Result
import ru.aasmc.wordify.common.core.domain.model.Word
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import javax.inject.Inject

class GetWordByName @Inject constructor(
    private val repository: WordRepository
) {
    suspend operator fun invoke(wordId: String): Result<Word> {
        val wordName = wordId.replaceFirstChar {
            if (it.isUpperCase()) it.lowercase() else it.toString()
        }
        return repository.getWordByName(wordName)
    }
}