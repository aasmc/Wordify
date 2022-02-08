package ru.aasmc.wordify.common.core.domain.repositories

import kotlinx.coroutines.flow.StateFlow

interface PreferencesRepository {

    val appThemeFlow: StateFlow<ThemePreference>
    var appTheme: ThemePreference

    val sortOrderFlow: StateFlow<Sort>
    var sortOrder: Sort

}
