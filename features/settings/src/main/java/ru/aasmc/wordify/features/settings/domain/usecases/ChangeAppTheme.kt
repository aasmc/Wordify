package ru.aasmc.wordify.features.settings.domain.usecases

import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import javax.inject.Inject

class ChangeAppTheme @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(themePreference: ThemePreference) {
        preferencesRepository.appTheme = themePreference
    }

}