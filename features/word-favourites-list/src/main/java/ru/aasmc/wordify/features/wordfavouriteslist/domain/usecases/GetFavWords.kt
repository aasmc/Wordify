package ru.aasmc.wordify.features.wordfavouriteslist.domain.usecases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.domain.model.Word
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import javax.inject.Inject

class GetFavWords @Inject constructor(
    private val repository: WordRepository
) {
    operator fun invoke(sort: Sort): Flow<PagingData<Word>> {
        return repository.getAllFavWords(sort = sort)
    }
}