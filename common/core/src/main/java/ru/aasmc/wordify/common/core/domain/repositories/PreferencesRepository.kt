package ru.aasmc.wordify.common.core.domain.repositories

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun saveSortOrder(sortOrder: Sort)

    suspend fun saveAppThemePreference(themePreference: ThemePreference)

    fun observeAppTheme(): Flow<ThemePreference>

    fun observeSortOrder(): Flow<Sort>

}
