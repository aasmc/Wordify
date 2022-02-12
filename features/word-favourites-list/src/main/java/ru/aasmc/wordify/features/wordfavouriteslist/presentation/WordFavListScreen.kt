package ru.aasmc.wordify.features.wordfavouriteslist.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import ru.aasmc.wordify.common.uicomponents.elements.WordListScreenCommon

@Composable
fun WordFavListScreen(
    wordListViewModel: WordFavListViewModel = hiltViewModel(),
    onExecuteSearch: (String) -> Unit,
    onWordClick: (Long) -> Unit
) {
    val sortOrder by wordListViewModel.getSortOrderFlow().collectAsState()
    WordListScreenCommon(
        sortOrder = sortOrder,
        wordListViewModel = wordListViewModel,
        onExecuteSearch = onExecuteSearch,
        onWordClick = onWordClick
    )
}
