package ru.aasmc.wordify.common.core.presentation.model

import ru.aasmc.wordify.common.core.utils.Event

data class WordsListErrorState(
    val failure: Event<Throwable>? = null
) {
    fun updateToHasFailure(t: Throwable): WordsListErrorState {
        return copy(
            failure = Event(t)
        )
    }
}
