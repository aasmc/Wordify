package ru.aasmc.wordify.common.core.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import javax.inject.Inject

class SearchRecentlySearchedWords @Inject constructor(
    private val repository: WordRepository
) {
    operator fun invoke(query: String): Flow<List<String>> {
        return repository.searchRecentlySearchedWords(query)
            .map { list ->
                list.map { it.word }
            }
    }
}