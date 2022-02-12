package ru.aasmc.wordify.features.wordfavouriteslist.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.uicomponents.elements.WordListScreenCommon

@Composable
fun WordFavListScreen(
    sortOrder: Sort,
    wordListViewModel: WordFavListViewModel = hiltViewModel(),
    onExecuteSearch: (String) -> Unit,
    onWordClick: (Long) -> Unit
) {
    WordListScreenCommon(
        sortOrder = sortOrder,
        wordListViewModel = wordListViewModel,
        onExecuteSearch = onExecuteSearch,
        onWordClick = onWordClick
    )
}
