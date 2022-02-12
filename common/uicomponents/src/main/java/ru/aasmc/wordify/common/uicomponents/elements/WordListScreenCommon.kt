package ru.aasmc.wordify.common.uicomponents.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.presentation.model.BaseViewModel
import ru.aasmc.wordify.common.core.presentation.model.WordsListErrorState
import ru.aasmc.wordify.common.core.presentation.model.WordsListEvent
import ru.aasmc.wordify.common.resources.R
import ru.aasmc.wordify.common.uicomponents.extensions.SwipeDismissSnackBarHost
import ru.aasmc.wordify.common.uicomponents.extensions.rememberFlowWithLifecycle

@Composable
fun WordListScreenCommon(
    sortOrder: Sort,
    wordListViewModel: BaseViewModel,
    onWordClick: (String) -> Unit
) {
    val viewState by rememberFlowWithLifecycle(flow = wordListViewModel.wordListErrorState)
        .collectAsState(initial = WordsListErrorState())
    val scaffoldState = rememberScaffoldState()
    viewState.failure?.let { errorEvent ->
        val unhandledMessage = errorEvent.getContentIfNotHandled() ?: return@let
        val errorMessage = stringResource(id = R.string.word_list_screen_error)
        LaunchedEffect(key1 = unhandledMessage) {
            scaffoldState.snackbarHostState.showSnackbar(
                errorMessage
            )
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { snackbarHostState ->
            SwipeDismissSnackBarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )
        }
    ) {
        WordListScreenInternal(
            sortOrder = sortOrder,
            viewModel = wordListViewModel,
            onWordClick = onWordClick
        )
    }
}

@Composable
private fun WordListScreenInternal(
    sortOrder: Sort,
    viewModel: BaseViewModel,
    onWordClick: (String) -> Unit
) {
    val searchStarted by viewModel.searchStarted.collectAsState()

    var query by remember {
        mutableStateOf("")
    }

    Column {
        WordSearchToolbar(
            onQueryChanged = {
                query = it
            },
            onExecuteSearch = { onWordClick(query) },
            onSearchStarted = {
                viewModel.handleEvent(
                    WordsListEvent.IsSearchingInProgress(it)
                )
            },
            onSaveRecentlySearchedWord = { word, time ->
                viewModel.handleEvent(
                    WordsListEvent.SaveRecentlySearchedWordEvent(word, time)
                )
            }
        )
        if (searchStarted) {
            val recentlySearchedWords = viewModel.searchRecentlySearchedFlow(query)
            SearchSurface(
                searchWordsFlow = recentlySearchedWords,
                onWordClick = onWordClick
            )
        } else {
            val pagingWords = viewModel.getWordListFlow(sortOrder)
            LazyPagingWordList(
                wordListPagingData = pagingWords,
                onWordClick = onWordClick,
                onchangeFavourite = { isFavourite, wordId ->
                    viewModel.handleEvent(
                        WordsListEvent.SetFavWordEvent(
                            wordId = wordId, isFavourite = !isFavourite
                        )
                    )
                }
            )
        }
    }
}