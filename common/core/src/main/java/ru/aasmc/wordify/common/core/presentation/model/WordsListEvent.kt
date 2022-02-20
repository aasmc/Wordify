package ru.aasmc.wordify.common.core.presentation.model

sealed class WordsListEvent {
    data class SetFavWordEvent(val wordId: Long, val isFavourite: Boolean): WordsListEvent()
    data class IsSearchingInProgress(val isInProgress: Boolean): WordsListEvent()
    data class SaveRecentlySearchedWordEvent(val word: String, val timeAdded: Long): WordsListEvent()
}
