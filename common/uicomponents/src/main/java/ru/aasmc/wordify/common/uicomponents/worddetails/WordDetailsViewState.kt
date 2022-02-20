package ru.aasmc.wordify.common.uicomponents.worddetails

import ru.aasmc.wordify.common.core.presentation.model.UIWord
import ru.aasmc.wordify.common.core.utils.Event
import ru.aasmc.wordify.common.core.utils.ProgressBarState

data class WordDetailsViewState(
    val progressBarState: ProgressBarState = ProgressBarState.IDLE,
    val uiWord: UIWord? = null,
    val errorReason: Event<String>? = null
) {
    fun updateToHasError(reason: String): WordDetailsViewState {
        return copy(errorReason = Event(reason))
    }

    fun updateToHasUiWord(uiWord: UIWord): WordDetailsViewState {
        return copy(uiWord = uiWord)
    }

    fun updateToLoadingState(progressBarState: ProgressBarState): WordDetailsViewState {
        return copy(progressBarState = progressBarState)
    }

}
