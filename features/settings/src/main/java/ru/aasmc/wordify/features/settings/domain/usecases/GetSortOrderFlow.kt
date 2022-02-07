package ru.aasmc.wordify.features.settings.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import javax.inject.Inject

class GetSortOrderFlow @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<Sort> {
        return preferencesRepository.observeSortOrder()
    }
}