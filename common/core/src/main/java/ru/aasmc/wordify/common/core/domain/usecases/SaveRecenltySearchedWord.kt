package ru.aasmc.wordify.common.core.domain.usecases

import ru.aasmc.wordify.common.core.domain.model.RecentlySearchedWord
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import javax.inject.Inject

class SaveRecentlySearchedWord @Inject constructor(
    private val repository: WordRepository
) {
    suspend operator fun invoke(timeAdded: Long, word: String) {
        repository.insertRecentlySearchedWord(RecentlySearchedWord(word, timeAdded))
    }
}