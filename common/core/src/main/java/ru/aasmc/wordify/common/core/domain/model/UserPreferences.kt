package ru.aasmc.wordify.common.core.domain.model

import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference

data class UserPreferences(
    val sortOrder: Sort = Sort.ASC_NAME,
    val appTheme: ThemePreference = ThemePreference.AUTO_THEME
)
