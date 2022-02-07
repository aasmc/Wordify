package ru.aasmc.wordify.common.core.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.aasmc.constants.PreferencesConstants
import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore
        by preferencesDataStore(PreferencesConstants.DATASTORE_NAME)

class WordifyPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesRepository {

    override suspend fun saveSortOrder(sortOrder: Sort) {
        context.dataStore.edit { preferences ->
            preferences[WORD_SORT_ORDER] = sortOrder.name
        }
    }

    override suspend fun saveAppThemePreference(themePreference: ThemePreference) {
        context.dataStore.edit { preferences ->
            preferences[THEME_PREFERENCE] = themePreference.name
        }
    }

    override fun observeAppTheme(): Flow<ThemePreference> {
        return context.dataStore.data
            .catch { t ->
                when (t) {
                    is IOException -> {
                        emit(emptyPreferences())
                    }
                    else -> throw t
                }
            }
            .map { preferences ->
                val theme = ThemePreference.valueOf(
                    preferences[THEME_PREFERENCE] ?: DEFAULT_THEME_PREFERENCE
                )
                theme
            }
    }

    override fun observeSortOrder(): Flow<Sort> {
        return context.dataStore.data
            .catch { t ->
                when (t) {
                    is IOException -> {
                        emit(emptyPreferences())
                    }
                    else -> throw t
                }
            }
            .map { preferences ->
                val sort = Sort.valueOf(preferences[WORD_SORT_ORDER] ?: DEFAULT_SORT_ORDER)
                sort
            }
    }

    companion object {
        private val WORD_SORT_ORDER = stringPreferencesKey("word_sort_preference")
        private val THEME_PREFERENCE = stringPreferencesKey("app_theme_preference")
        private const val DEFAULT_SORT_ORDER = "ASC_NAME"
        private const val DEFAULT_THEME_PREFERENCE = "AUTO_THEME"
    }
}
















