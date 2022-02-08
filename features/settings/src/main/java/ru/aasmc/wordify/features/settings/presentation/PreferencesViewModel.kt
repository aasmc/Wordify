package ru.aasmc.wordify.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.features.settings.domain.usecases.ChangeAppTheme
import ru.aasmc.wordify.features.settings.domain.usecases.ChangeWordSortOrder
import ru.aasmc.wordify.features.settings.domain.usecases.GetSortOrderFlow
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val changeAppTheme: ChangeAppTheme,
    private val changeWordSortOrder: ChangeWordSortOrder,
) : ViewModel() {

    fun handleEvent(event: UserPrefsEvent) {
        when (event) {
            is UserPrefsEvent.ChangeSortOrder -> {
                changeWordSortOrder(event.sortOrder)
            }
            is UserPrefsEvent.ChangeTheme -> {
                changeAppTheme(event.themePreference)
            }
        }
    }
}