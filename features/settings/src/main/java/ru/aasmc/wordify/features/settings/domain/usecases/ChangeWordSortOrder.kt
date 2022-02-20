package ru.aasmc.wordify.features.settings.domain.usecases

import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import javax.inject.Inject

class ChangeWordSortOrder @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(sortOrder: Sort) {
        preferencesRepository.sortOrder = sortOrder
    }

}