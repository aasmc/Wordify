package ru.aasmc.wordify.common.core.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.domain.model.UserPreferences

interface PreferencesRepository {

    suspend fun saveUserPreference(userPreferences: UserPreferences)

    fun getUserPreferences(): Flow<UserPreferences>

}