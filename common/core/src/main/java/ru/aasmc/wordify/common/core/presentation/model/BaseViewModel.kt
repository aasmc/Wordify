package ru.aasmc.wordify.common.core.presentation.model

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.aasmc.wordify.common.core.domain.repositories.Sort

interface BaseViewModel {

    val wordListErrorState: StateFlow<WordsListErrorState>

    val searchStarted: StateFlow<Boolean>

    fun searchRecentlySearchedFlow(query: String): StateFlow<List<String>>

    fun getWordListFlow(sort: Sort): Flow<PagingData<UIWord>>

    fun handleEvent(event: WordsListEvent)

}