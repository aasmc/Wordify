package ru.aasmc.wordify.features.settings.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.domain.model.UserPreferences
import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import javax.inject.Inject

class GetUserPreferencesFlow @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(): Flow<UserPreferences> {
        return preferencesRepository.getUserPreferences()
    }

}