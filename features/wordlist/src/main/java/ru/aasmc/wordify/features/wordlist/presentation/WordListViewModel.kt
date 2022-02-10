package ru.aasmc.wordify.features.wordlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.usecases.SearchWords
import ru.aasmc.wordify.common.core.domain.usecases.SetWordFavourite
import ru.aasmc.wordify.common.core.presentation.model.UIWord
import ru.aasmc.wordify.features.wordlist.domain.usecases.GetWordsList
import javax.inject.Inject

@HiltViewModel
class WordListViewModel @Inject constructor(
    private val getWordsList: GetWordsList,
    private val searchWords: SearchWords,
    private val setWordFavourite: SetWordFavourite,
) : ViewModel() {

    private val _wordListErrorState =
        MutableStateFlow(WordsListErrorState())
    val wordListErrorState: StateFlow<WordsListErrorState> = _wordListErrorState.asStateFlow()

    private val _searchStarted =
        MutableStateFlow(false)
    val searchStarted: StateFlow<Boolean> = _searchStarted.asStateFlow()

    fun getWordListFlow(sort: Sort): Flow<PagingData<UIWord>> {
        return getWordsList(sort = sort).map { pagingData ->
            pagingData.map {
                UIWord.fromDomain(it)
            }
        }
            .distinctUntilChanged()
    }

    fun getSearchWordFlow(query: String, sort: Sort): Flow<PagingData<UIWord>> {
        return searchWords(name = query, sort = sort).map { pagingData ->
            pagingData.map {
                UIWord.fromDomain(it)
            }
        }
            .distinctUntilChanged()
    }

    fun handleEvent(event: WordsListEvent) {
        when (event) {
            is WordsListEvent.SetFavWordEvent -> {
                viewModelScope.launch {
                    try {
                        setWordFavourite(event.wordId, event.isFavourite)
                    } catch (t: Throwable) {
                        when (t) {
                            is CancellationException -> throw t
                            else -> {
                                _wordListErrorState.value =
                                    _wordListErrorState.value.updateToHasFailure(t)
                            }
                        }
                    }
                }
            }
            is WordsListEvent.IsSearchingInProgress -> {
                _searchStarted.value = event.isInProgress
            }
        }
    }
}






















