package ru.aasmc.wordify.common.uicomponents.elements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.flow.Flow
import ru.aasmc.wordify.common.core.presentation.model.UIWord
import ru.aasmc.wordify.common.resources.R

@Composable
fun LazyPagingWordList(
    wordListPagingData: Flow<PagingData<UIWord>>,
    onWordClick: (String) -> Unit,
    onchangeFavourite: (Boolean, String) -> Unit
) {
    val wordList = wordListPagingData.collectAsLazyPagingItems()
    LazyColumn {
        items(wordList) { uiWordNullable ->
            uiWordNullable?.let { uiWord ->
                val firstProperty = uiWord.wordProperties[0]
                WordItemCard(
                    wordName = uiWord.wordId,
                    partOfSpeech = firstProperty.partOfSpeech,
                    pronunciation = uiWord.pronunciation,
                    description = firstProperty.definition,
                    isFavourite = uiWord.isFavourite,
                    onChangeFavourite = {
                        onchangeFavourite(uiWord.isFavourite, uiWord.wordId)
                    },
                    onWordItemClick = {
                        onWordClick(uiWord.wordId)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        wordList.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                }
                loadState.append is LoadState.Loading -> {
                    item { LoadingItem() }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = wordList.loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = wordList.loadState.append as LoadState.Error
                    item {
                        ErrorItem(
                            message = stringResource(id = R.string.error_loading_words),
                            onClickRetry = { retry() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingItem(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
private fun LoadingView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            maxLines = 1,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onError
        )
        OutlinedButton(
            onClick = onClickRetry,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.error,
                contentColor = MaterialTheme.colors.onError
            )
        ) {
            Text(text = stringResource(id = R.string.error_button))
        }
    }
}

















