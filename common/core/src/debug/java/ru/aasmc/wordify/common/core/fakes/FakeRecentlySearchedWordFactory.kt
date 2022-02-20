package ru.aasmc.wordify.common.core.fakes

import ru.aasmc.wordify.common.core.domain.model.RecentlySearchedWord
import java.time.Instant

object FakeRecentlySearchedWordFactory {

    fun createRecentlySearchedWord(id: Int): RecentlySearchedWord {
        return RecentlySearchedWord(
            word = "Word $id",
            timeAdded = Instant.now().toEpochMilli()
        )
    }

}