package ru.aasmc.wordify.common.core.fakes

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import javax.inject.Inject

class FakePreferencesRepository @Inject constructor(): PreferencesRepository {

    private val _appThemeFlow = MutableStateFlow(ThemePreference.AUTO_THEME)
    private val _sortOrderFlow = MutableStateFlow(Sort.ASC_NAME)

    override val appThemeFlow: StateFlow<ThemePreference> = _appThemeFlow.asStateFlow()

    override var appTheme: ThemePreference
        get() = appThemeFlow.value
        set(value) {
            _appThemeFlow.value = value
        }

    override val sortOrderFlow: StateFlow<Sort> = _sortOrderFlow.asStateFlow()

    override var sortOrder: Sort
        get() = sortOrderFlow.value
        set(value) {
            _sortOrderFlow.value = value
        }
}