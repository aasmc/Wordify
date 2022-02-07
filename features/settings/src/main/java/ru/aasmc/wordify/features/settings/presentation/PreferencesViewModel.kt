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
    getSortOrderFlow: GetSortOrderFlow
) : ViewModel() {

    val sortOrder: StateFlow<Sort> = getSortOrderFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Sort.ASC_NAME
        )

    private val _uiState: MutableStateFlow<PreferencesUiState> =
        MutableStateFlow(PreferencesUiState.Empty)
    val uiState: StateFlow<PreferencesUiState> = _uiState.asStateFlow()


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