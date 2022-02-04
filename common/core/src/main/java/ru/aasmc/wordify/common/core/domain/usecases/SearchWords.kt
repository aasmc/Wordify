package ru.aasmc.wordify.common.core.domain.usecases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.domain.model.Word
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import javax.inject.Inject

class SearchWords @Inject constructor(
    private val repository: WordRepository
) {
    operator fun invoke(name: String, sort: Sort): Flow<PagingData<Word>> {
        return repository.searchWord(name, sort)
    }
}