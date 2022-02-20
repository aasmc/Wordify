package ru.aasmc.wordify.common.core.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.aasmc.wordify.common.core.domain.Result
import ru.aasmc.wordify.common.core.domain.model.Word
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import ru.aasmc.wordify.common.core.utils.ProgressBarState
import javax.inject.Inject

class GetWordDetailsFlow @Inject constructor(
    private val repository: WordRepository
) {
    operator fun invoke(wordName: String): Flow<Result<Word>> {
        val wordNameLowerCase = wordName.replaceFirstChar {
            if (it.isUpperCase()) it.lowercase() else it.toString()
        }
        return flow {
            emit(Result.Loading(progressBarState = ProgressBarState.LOADING))
            emit(repository.getWordByName(wordNameLowerCase))
            emit(Result.Loading(progressBarState = ProgressBarState.IDLE))
        }
    }
}