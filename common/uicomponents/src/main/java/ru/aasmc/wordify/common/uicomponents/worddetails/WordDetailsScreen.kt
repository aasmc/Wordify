package ru.aasmc.wordify.common.uicomponents.worddetails

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.aasmc.wordify.common.core.presentation.model.UIWord
import ru.aasmc.wordify.common.core.utils.ProgressBarState.LOADING
import ru.aasmc.wordify.common.resources.R
import ru.aasmc.wordify.common.uicomponents.extensions.SwipeDismissSnackBarHost

@Composable
fun WordDetailsScreen(
   viewModel: WordDetailsViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val viewState by viewModel.viewState.collectAsState()
    viewState.errorReason?.let { errorEvent ->
        val unhandledError = errorEvent.getContentIfNotHandled() ?: return@let
        val errorMessage = stringResource(id = R.string.get_word_error)
        LaunchedEffect(key1 = unhandledError) {
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
        WordDetailsInternal(viewState = viewState)
    }
}

@Composable
private fun WordDetailsInternal(
    viewState: WordDetailsViewState
) {
    if (viewState.progressBarState == LOADING) {
        WordLoading()
    } else {
        val uiWord = viewState.uiWord
        if (uiWord == null) {
            WordError()
        } else {
            WordSuccess(uiWord = uiWord)
        }
    }
}

@Composable
private fun WordSuccess(
    uiWord: UIWord
) {
    Text(text = uiWord.wordName)
}

@Composable
private fun WordError() {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.get_word_error),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun WordLoading() {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.onSurface
        )
    }
}

















