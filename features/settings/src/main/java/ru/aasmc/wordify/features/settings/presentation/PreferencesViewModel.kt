package ru.aasmc.wordify.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.aasmc.wordify.features.settings.domain.usecases.ChangeAppTheme
import ru.aasmc.wordify.features.settings.domain.usecases.ChangeWordSortOrder
import ru.aasmc.wordify.features.settings.domain.usecases.GetUserPreferencesFlow
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val changeAppTheme: ChangeAppTheme,
    private val changeWordSortOrder: ChangeWordSortOrder,
    getUserPreferences: GetUserPreferencesFlow
) : ViewModel() {

    private val _uiState: MutableStateFlow<PreferencesUiState> =
        MutableStateFlow(PreferencesUiState.Empty)
    val uiState: StateFlow<PreferencesUiState>
        get() = _uiState.asStateFlow()

    val userPreferencesFlow = getUserPreferences()

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

    private fun safeHandleEvent(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
                handleSuccess()
            } catch (t: Throwable) {
                handleError()
            }
        }
    }

    private fun handleSuccess() {
        _uiState.value = PreferencesUiState.Empty
    }

    private fun handleError() {
        _uiState.value = PreferencesUiState.Failure
    }

}