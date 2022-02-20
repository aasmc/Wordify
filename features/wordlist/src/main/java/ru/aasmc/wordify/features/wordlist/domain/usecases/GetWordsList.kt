package ru.aasmc.wordify.features.wordlist.domain.usecases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.domain.model.Word
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import javax.inject.Inject

class GetWordsList @Inject constructor(
    private val wordRepository: WordRepository
) {
    operator fun invoke(sort: Sort): Flow<PagingData<Word>> {
        return wordRepository.getAllWords(sort = sort)
    }
}