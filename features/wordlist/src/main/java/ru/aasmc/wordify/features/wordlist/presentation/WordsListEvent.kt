package ru.aasmc.wordify.features.wordlist.presentation

sealed class WordsListEvent {
    data class SetFavWordEvent(val wordId: String, val isFavourite: Boolean): WordsListEvent()
    data class IsSearchingInProgress(val isInProgress: Boolean): WordsListEvent()
}
