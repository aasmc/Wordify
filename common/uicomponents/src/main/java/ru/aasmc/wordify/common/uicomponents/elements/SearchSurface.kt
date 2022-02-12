package ru.aasmc.wordify.common.uicomponents.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SearchSurface(
    searchWordsFlow: StateFlow<List<String>>,
    onWordClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val searchedWords by searchWordsFlow.collectAsState()
        LazyColumn {
            items(searchedWords.take(10)) { word ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onWordClick(word)
                        }
                ) {
                    Text(
                        text = word,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}



















