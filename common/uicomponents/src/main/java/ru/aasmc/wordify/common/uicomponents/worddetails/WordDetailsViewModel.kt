package ru.aasmc.wordify.common.uicomponents.worddetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.aasmc.constants.ExceptionMessage
import ru.aasmc.constants.WordConstants
import ru.aasmc.wordify.common.core.domain.Result
import ru.aasmc.wordify.common.core.domain.usecases.GetWordDetailsFlow
import ru.aasmc.wordify.common.core.domain.usecases.SetWordFavourite
import ru.aasmc.wordify.common.core.presentation.model.UIWord
import javax.inject.Inject

@HiltViewModel
class WordDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getWordByNameFlow: GetWordDetailsFlow,
    private val setWordFavourite: SetWordFavourite
) : ViewModel() {

    private val _viewState = MutableStateFlow(WordDetailsViewState())
    val viewState: StateFlow<WordDetailsViewState> = _viewState.asStateFlow()
    init {
        // all of the arguments that are passed to the destination
        // through the back stack entry are automatically saved in the
        // savedStateHandle, and we can access them by their names.
        savedStateHandle.get<String>(WordConstants.WORD_ID_ARGUMENT)?.let { wordArg ->
            handleEvent(WordDetailsEvent.GetWordDetailsEvent(wordArg))
        }
    }

    fun handleEvent(event: WordDetailsEvent) {
        when (event) {
            is WordDetailsEvent.GetWordDetailsEvent -> {
                getWordDetails(event.wordName)
            }
            is WordDetailsEvent.SetWordFavourite -> {
                safeHandleEvent {
                    setWordFavourite(
                        wordId = event.wordId, favourite = event.isFavourite
                    )
                }
            }
        }
    }

    private fun getWordDetails(wordName: String) {
        getWordByNameFlow(wordName)
            .onEach { result ->
                when (result) {
                    is Result.Failure -> _viewState.value =
                        _viewState.value.updateToHasError(result.reason)
                    is Result.Loading -> _viewState.value =
                        _viewState.value.updateToLoadingState(result.progressBarState)
                    is Result.Success -> _viewState.value =
                        _viewState.value.updateToHasUiWord(UIWord.fromDomain(result.data))
                }
            }.launchIn(viewModelScope)
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
                    _viewState.value.updateToHasError(ExceptionMessage.FAILED_TO_SET_WORD_FAVOURITE)
                }
            }
        }
    }
}



















