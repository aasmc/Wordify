package ru.aasmc.wordify.common.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.aasmc.wordify.common.core.domain.model.UserPreferences
import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import java.io.IOException
import javax.inject.Inject

class WordifyPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesRepository {

    override suspend fun saveSortOrder(sortOrder: Sort) {
        dataStore.edit { preferences ->
            preferences[WORD_SORT_ORDER] = sortOrder.name
        }
    }

    override suspend fun saveAppThemePreference(themePreference: ThemePreference) {
        dataStore.edit { preferences ->
            preferences[THEME_PREFERENCE] = themePreference.name
        }
    }

    override fun getUserPreferences(): Flow<UserPreferences> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                val sort = Sort.valueOf(preferences[WORD_SORT_ORDER] ?: DEFAULT_SORT_ORDER)
                val theme = ThemePreference.valueOf(
                    preferences[THEME_PREFERENCE] ?: DEFAULT_THEME_PREFERENCE
                )
                UserPreferences(
                    sortOrder = sort,
                    appTheme = theme
                )
            }
    }

    companion object {
        private val WORD_SORT_ORDER = stringPreferencesKey("word_sort_preference")
        private val THEME_PREFERENCE = stringPreferencesKey("app_theme_preference")
        private const val DEFAULT_SORT_ORDER = "ASC_NAME"
        private const val DEFAULT_THEME_PREFERENCE = "AUTO_THEME"
    }
}
















