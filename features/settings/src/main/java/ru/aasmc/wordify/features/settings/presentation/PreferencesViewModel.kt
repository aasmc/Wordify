package ru.aasmc.wordify.features.settings.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.usecases.GetSortOrderFlow
import ru.aasmc.wordify.features.settings.domain.usecases.ChangeAppTheme
import ru.aasmc.wordify.features.settings.domain.usecases.ChangeWordSortOrder
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val changeAppTheme: ChangeAppTheme,
    private val changeWordSortOrder: ChangeWordSortOrder,
    private val getSortOrderFlow: GetSortOrderFlow
) : ViewModel() {

    private val _uiStateFlow =
        MutableStateFlow<PreferencesUiState>(PreferencesUiState())
    val uiStateFlow: StateFlow<PreferencesUiState> = _uiStateFlow.asStateFlow()

    fun getSortOrderStateFlow(): StateFlow<Sort> {
        return getSortOrderFlow()
    }

    fun handleEvent(event: UserPrefsEvent) {
        when (event) {
            is UserPrefsEvent.ChangeSortOrder -> {
                safeHandleEvent {
                    changeWordSortOrder(event.sortOrder)
                }
            }
            is UserPrefsEvent.ChangeTheme -> {
                safeHandleEvent {
                    changeAppTheme(event.themePreference)
                }
            }
        }
    }

    private fun safeHandleEvent(block: () -> Unit) {
        try {
            block()
        } catch (t: Throwable) {
            _uiStateFlow.value = _uiStateFlow.value.updateToHasFailure(Throwable())
        }
    }
}