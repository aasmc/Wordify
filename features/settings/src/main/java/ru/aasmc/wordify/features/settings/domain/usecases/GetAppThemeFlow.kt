package ru.aasmc.wordify.features.settings.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import javax.inject.Inject

class GetAppThemeFlow @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(): Flow<ThemePreference> {
        return preferencesRepository.observeAppTheme()
    }
}