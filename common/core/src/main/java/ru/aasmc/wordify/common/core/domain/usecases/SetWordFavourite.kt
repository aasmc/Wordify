package ru.aasmc.wordify.common.core.domain.usecases

import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import javax.inject.Inject

class SetWordFavourite @Inject constructor(
    private val repository: WordRepository
) {

    suspend operator fun invoke(wordId: String, favourite: Boolean) {
        repository.setFavourite(wordId, favourite)
    }

}