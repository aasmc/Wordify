package ru.aasmc.wordify.common.uicomponents.worddetails

sealed class WordDetailsEvent {
    data class GetWordDetailsEvent(val wordName: String): WordDetailsEvent()
}
