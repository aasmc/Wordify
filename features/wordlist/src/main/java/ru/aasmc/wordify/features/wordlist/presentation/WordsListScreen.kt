package ru.aasmc.wordify.features.wordlist.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.presentation.model.WordsListEvent
import ru.aasmc.wordify.common.uicomponents.elements.WordListScreenCommon

@Composable
fun WordListScreen(
    wordListViewModel: WordListViewModel = hiltViewModel(),
    onWordClick: (String) -> Unit
) {
    val sortOrder by wordListViewModel.getSortOrderFlow().collectAsState()
    WordListScreenCommon(
        sortOrder = sortOrder,
        wordListViewModel = wordListViewModel,
        onWordClick = onWordClick
    )
}

























