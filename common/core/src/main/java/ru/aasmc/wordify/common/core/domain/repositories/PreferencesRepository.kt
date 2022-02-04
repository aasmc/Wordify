package ru.aasmc.wordify.common.core.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.domain.model.UserPreferences

interface PreferencesRepository {

    suspend fun saveSortOrder(sortOrder: Sort)

    suspend fun saveAppThemePreference(themePreference: ThemePreference)

    fun getUserPreferences(): Flow<UserPreferences>

}