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
import ru.aasmc.wordify.common.core.domain.usecases.SaveRecentlySearchedWord
import ru.aasmc.wordify.common.core.domain.usecases.SearchRecentlySearchedWords
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
    private val searchRecentlySearchedWords: SearchRecentlySearchedWords,
    private val saveRecentlySearchedWord: SaveRecentlySearchedWord,
) : ViewModel() {

    private val _wordListErrorState =
        MutableStateFlow(WordsListErrorState())
    val wordListErrorState: StateFlow<WordsListErrorState> = _wordListErrorState.asStateFlow()

    private val _recentlySearchedFlow =
        MutableStateFlow("")
    val recentlySearchedFlow: StateFlow<String> = _recentlySearchedFlow.asStateFlow()

    private val _searchStarted =
        MutableStateFlow(false)
    val searchStarted: StateFlow<Boolean> = _searchStarted.asStateFlow()

    fun searchRecentlySearchedFlow(query: String): StateFlow<List<String>> {
        return searchRecentlySearchedWords(query)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = emptyList()
            )
    }

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
                    safeHandleEvent {
                        setWordFavourite(event.wordId, event.isFavourite)
                    }
                }
            }
            is WordsListEvent.IsSearchingInProgress -> {
                _searchStarted.value = event.isInProgress
            }
            is WordsListEvent.SaveRecentlySearchedWordEvent -> {
                viewModelScope.launch {
                    safeHandleEvent {
                        saveRecentlySearchedWord(event.timeAdded, event.word)
                    }
                }
            }
        }
    }

    private suspend fun safeHandleEvent(block: suspend () -> Unit) {
        try {
            block()
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






















