package ru.aasmc.wordify.common.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.aasmc.wordify.common.core.data.preferences.WordifyPreferences
import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import java.io.File

@ExperimentalCoroutinesApi
class WordifyPreferencesTest {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var preferenceScope: CoroutineScope
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var preferences: PreferencesRepository

    @Before
    fun createDataStore() {
        preferenceScope = CoroutineScope(testDispatcher + Job())
        dataStore = PreferenceDataStoreFactory.create(scope = preferenceScope) {
            InstrumentationRegistry.getInstrumentation().targetContext.preferencesDataStoreFile(
                "test-preferences-file"
            )
        }
        preferences = WordifyPreferences(dataStore)
    }

    @After
    fun removeDataStore() {
        File(
            ApplicationProvider.getApplicationContext<Context>().filesDir,
            "datastore"
        ).deleteRecursively()
        preferenceScope.cancel()
    }

    @Before
    fun setMainDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun resetMainDispatcher() {
        Dispatchers.resetMain()
    }

    fun coTest(block: suspend TestScope.() -> Unit) =
        testScope.runTest {
            block()
        }

    @Test
    fun should_return_auto_theme_by_default() = coTest {
        val userPrefs = preferences.getUserPreferences().take(1).single()
        assertEquals(ThemePreference.AUTO_THEME, userPrefs.appTheme)
    }

    @Test
    fun should_return_sortByNameAsc_by_default() = coTest {
        val userPrefs = preferences.getUserPreferences().take(1).single()
        assertEquals(Sort.ASC_NAME, userPrefs.sortOrder)
    }

    @Test
    fun should_change_app_theme_to_dark() = coTest {
        preferences.saveAppThemePreference(ThemePreference.DARK_THEME)
        val userPrefs = preferences.getUserPreferences().take(1).single()
        assertEquals(ThemePreference.DARK_THEME, userPrefs.appTheme)
    }

    @Test
    fun should_change_app_theme_to_light() = coTest {
        preferences.saveAppThemePreference(ThemePreference.LIGHT_THEME)
        val userPrefs = preferences.getUserPreferences().take(1).single()
        assertEquals(ThemePreference.LIGHT_THEME, userPrefs.appTheme)
    }

    @Test
    fun should_change_sort_order_to_name_desc() = coTest {
        preferences.saveSortOrder(Sort.DESC_NAME)
        val userPrefs = preferences.getUserPreferences().take(1).single()
        assertEquals(Sort.DESC_NAME, userPrefs.sortOrder)
    }

    @Test
    fun should_change_sort_order_to_timeAdded_asc() = coTest {
        preferences.saveSortOrder(Sort.ASC_TIME)
        val userPrefs = preferences.getUserPreferences().take(1).single()
        assertEquals(Sort.ASC_TIME, userPrefs.sortOrder)
    }

    @Test
    fun should_change_sort_order_to_timeAdded_desc() = coTest {
        preferences.saveSortOrder(Sort.DESC_TIME)
        val userPrefs = preferences.getUserPreferences().take(1).single()
        assertEquals(Sort.DESC_TIME, userPrefs.sortOrder)
    }
}




















