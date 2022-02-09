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

    private val _uiStateFlow =
        MutableStateFlow<PreferencesUiState>(PreferencesUiState())
    val uiStateFlow: StateFlow<PreferencesUiState> = _uiStateFlow.asStateFlow()

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