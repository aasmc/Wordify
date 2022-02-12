package ru.aasmc.wordify.common.uicomponents.worddetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.aasmc.constants.WordConstants
import ru.aasmc.wordify.Logger
import ru.aasmc.wordify.common.core.domain.Result
import ru.aasmc.wordify.common.core.domain.usecases.GetWordByName
import ru.aasmc.wordify.common.core.domain.usecases.GetWordDetailsFlow
import ru.aasmc.wordify.common.core.presentation.model.UIWord
import javax.inject.Inject

@HiltViewModel
class WordDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getWordByNameFlow: GetWordDetailsFlow,
) : ViewModel() {

    private val _viewState = MutableStateFlow(WordDetailsViewState())
    val viewState: StateFlow<WordDetailsViewState> = _viewState.asStateFlow()

    init {
        // all of the arguments that are passed to the destination
        // through the back stack entry are automatically saved in the
        // savedStateHandle, and we can access them by their names.
        savedStateHandle.get<String>(WordConstants.WORD_ID_ARGUMENT)?.let { wordName ->
            handleEvent(WordDetailsEvent.GetWordDetailsEvent(wordName))
        }
    }

    private fun handleEvent(event: WordDetailsEvent) {
        when (event) {
            is WordDetailsEvent.GetWordDetailsEvent -> {
                getWordDetails(event.wordName)
            }
        }
    }

    private fun getWordDetails(wordName: String) {
        getWordByNameFlow(wordName)
            .onEach { result ->
                when (result) {
                    is Result.Failure -> _viewState.value = _viewState.value.updateToHasError(result.reason)
                    is Result.Loading -> _viewState.value = _viewState.value.updateToLoadingState(result.progressBarState)
                    is Result.Success -> _viewState.value = _viewState.value.updateToHasUiWord(UIWord.fromDomain(result.data))
                }
            }.launchIn(viewModelScope)
    }
}



















