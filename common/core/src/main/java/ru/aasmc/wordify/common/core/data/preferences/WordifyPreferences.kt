package ru.aasmc.wordify.common.core.data.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class WordifyPreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : PreferencesRepository {
    override val appThemeFlow: MutableStateFlow<ThemePreference>

    override var appTheme: ThemePreference by AppThemeDelegate(
        appThemeKey = "app_theme_key",
        defaultTheme = ThemePreference.AUTO_THEME
    )

    override val sortOrderFlow: MutableStateFlow<Sort>

    override var sortOrder: Sort by SortOrderDelegate(
        sortOrderKey = "sort_order_key",
        defaultSortOrder = Sort.ASC_NAME
    )

    init {
        appThemeFlow = MutableStateFlow(appTheme)
        sortOrderFlow = MutableStateFlow(sortOrder)
    }

    inner class SortOrderDelegate(
        private val sortOrderKey: String,
        private val defaultSortOrder: Sort
    ) : ReadWriteProperty<Any?, Sort> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Sort =
            Sort.fromOrdinal(sharedPreferences.getInt(sortOrderKey, defaultSortOrder.ordinal))

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Sort) {
            sortOrderFlow.value = value
            sharedPreferences.edit {
                putInt(sortOrderKey, value.ordinal)
            }
        }
    }

    inner class AppThemeDelegate(
        private val appThemeKey: String,
        private val defaultTheme: ThemePreference
    ) : ReadWriteProperty<Any?, ThemePreference> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): ThemePreference =
            ThemePreference.fromOrdinal(sharedPreferences.getInt(appThemeKey, defaultTheme.ordinal))

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: ThemePreference) {
            appThemeFlow.value = value
            sharedPreferences.edit {
                putInt(appThemeKey, value.ordinal)
            }
        }
    }

}
















