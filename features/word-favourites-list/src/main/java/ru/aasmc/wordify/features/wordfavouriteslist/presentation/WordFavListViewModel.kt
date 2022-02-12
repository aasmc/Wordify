package ru.aasmc.wordify.features.wordfavouriteslist.presentation

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
import ru.aasmc.wordify.common.core.domain.usecases.SetWordFavourite
import ru.aasmc.wordify.common.core.presentation.model.*
import ru.aasmc.wordify.features.wordfavouriteslist.domain.usecases.GetFavWords
import javax.inject.Inject

@HiltViewModel
class WordFavListViewModel @Inject constructor(
    private val getFavWordsList: GetFavWords,
    private val setWordFavourite: SetWordFavourite,
    private val searchRecentlySearchedWords: SearchRecentlySearchedWords,
    private val saveRecentlySearchedWord: SaveRecentlySearchedWord,
) : ViewModel(), BaseViewModel {

    private val _wordListErrorState =
        MutableStateFlow(WordsListErrorState())
    override val wordListErrorState: StateFlow<WordsListErrorState> = _wordListErrorState.asStateFlow()

    private val _searchStarted =
        MutableStateFlow(false)
    override val searchStarted: StateFlow<Boolean> = _searchStarted.asStateFlow()

    override fun searchRecentlySearchedFlow(query: String): StateFlow<List<String>> {
        return searchRecentlySearchedWords(query)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = emptyList()
            )
    }

    override fun getWordListFlow(sort: Sort): Flow<PagingData<UIWord>> {
        return getFavWordsList(sort = sort).map { pagingData ->
            pagingData.map {
                UIWord.fromDomain(it)
            }
        }.distinctUntilChanged()
    }

    override fun handleEvent(event: WordsListEvent) {
        when (event) {
            is WordsListEvent.IsSearchingInProgress -> {
                _searchStarted.value = event.isInProgress
            }
            is WordsListEvent.SaveRecentlySearchedWordEvent -> {
                safeHandleEvent {
                    saveRecentlySearchedWord(event.timeAdded, event.word)
                }
            }
            is WordsListEvent.SetFavWordEvent -> {
                safeHandleEvent {
                    setWordFavourite(event.wordId, event.isFavourite)
                }
            }
        }
    }

    private fun safeHandleEvent(block: suspend () -> Unit) {
        try {
            viewModelScope.launch {
                block()
            }
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