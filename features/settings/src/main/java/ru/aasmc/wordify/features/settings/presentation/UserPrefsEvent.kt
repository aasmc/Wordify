package ru.aasmc.wordify.features.settings.presentation

import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference

sealed class UserPrefsEvent {
    data class ChangeTheme(val themePreference: ThemePreference) : UserPrefsEvent()
    data class ChangeSortOrder(val sortOrder: Sort) : UserPrefsEvent()
}
