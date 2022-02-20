package ru.aasmc.wordify.common.uicomponents.elements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.flow.Flow
import ru.aasmc.constants.WordListConstants
import ru.aasmc.wordify.common.core.presentation.model.UIWord
import ru.aasmc.wordify.common.resources.R

@Composable
fun LazyPagingWordList(
    wordListPagingData: Flow<PagingData<UIWord>>,
    onWordClick: (String) -> Unit,
    onchangeFavourite: (Boolean, Long) -> Unit
) {
    val wordList = wordListPagingData.collectAsLazyPagingItems()
    if (wordList.itemCount == 0) {
        EmptyPlaceholder()
    } else {
        LazyColumn {
            items(wordList) { uiWordNullable ->
                uiWordNullable?.let { uiWord ->
                    val properties = uiWord.wordProperties
                    var partOfSpeech = ""
                    var definition = ""
                    if (properties.isNotEmpty()) {
                        val randomProperty = properties[0]
                        partOfSpeech = randomProperty.partOfSpeech
                        definition = randomProperty.definition
                    }
                    WordItemCard(
                        wordName = uiWord.wordName,
                        partOfSpeech = partOfSpeech,
                        pronunciation = uiWord.pronunciation,
                        description = definition,
                        isFavourite = uiWord.isFavourite,
                        onChangeFavourite = {
                            onchangeFavourite(uiWord.isFavourite, uiWord.wordId)
                        },
                        onWordItemClick = {
                            onWordClick(uiWord.wordName)
                        },
                        modifier = Modifier
                            .testTag(WordListConstants.WORD_ITEM_CARD_TAD)
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

@Composable
private fun EmptyPlaceholder() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize(0.9f)
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(id = R.string.empty_search_icon),
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(64.dp)
            )
            Text(
                text = stringResource(id = R.string.empty_search_label),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(horizontal = 32.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

















