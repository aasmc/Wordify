package ru.aasmc.wordify.features.settings.presentation

sealed class PreferencesUiState {
    object Empty: PreferencesUiState()
    object Failure: PreferencesUiState()
}
