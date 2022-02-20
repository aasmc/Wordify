package ru.aasmc.wordify.common.core.domain.usecases

import kotlinx.coroutines.flow.StateFlow
import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import javax.inject.Inject

class GetSortOrderFlow @Inject constructor(
    private val repository: PreferencesRepository
) {
    operator fun invoke(): StateFlow<Sort> {
        return repository.sortOrderFlow
    }
}