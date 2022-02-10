package ru.aasmc.wordify.features.wordlist.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.uicomponents.elements.LazyPagingWordList
import ru.aasmc.wordify.common.uicomponents.elements.WordSearchToolbar
import ru.aasmc.wordify.common.uicomponents.extensions.SwipeDismissSnackBarHost
import ru.aasmc.wordify.common.uicomponents.extensions.rememberFlowWithLifecycle
import ru.aasmc.wordify.features.wordlist.R

@Composable
fun WordListScreen(
    sortOrder: Sort,
    wordListViewModel: WordListViewModel = hiltViewModel(),
    onExecuteSearch: (String) -> Unit,
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
            onExecuteSearch = onExecuteSearch,
            onWordClick = onWordClick
        )
    }
}

@Composable
private fun WordListScreenInternal(
    sortOrder: Sort,
    viewModel: WordListViewModel,
    onExecuteSearch: (String) -> Unit,
    onWordClick: (String) -> Unit
) {
    val searchStarted by viewModel.searchStarted.collectAsState()

    var query by remember {
        mutableStateOf("")
    }

    val pagingWords = if (searchStarted) {
        viewModel.getSearchWordFlow(query, sortOrder)
    } else {
        viewModel.getWordListFlow(sortOrder)
    }

    Column {
        WordSearchToolbar(
            onQueryChanged = {
                query = it
            },
            onExecuteSearch = { onExecuteSearch(query) },
            onSearchStarted = {
                viewModel.handleEvent(
                    WordsListEvent.IsSearchingInProgress(it)
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
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
























