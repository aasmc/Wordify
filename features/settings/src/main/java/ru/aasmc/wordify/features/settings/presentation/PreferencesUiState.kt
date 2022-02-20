package ru.aasmc.wordify.features.settings.presentation

import ru.aasmc.wordify.common.core.utils.Event

data class PreferencesUiState(
    val failure: Event<Throwable>? = null
) {
    fun updateToHasFailure(t: Throwable): PreferencesUiState {
        return copy(
            failure = Event(t)
        )
    }
}
