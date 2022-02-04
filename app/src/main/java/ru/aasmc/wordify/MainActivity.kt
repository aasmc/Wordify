package ru.aasmc.wordify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import ru.aasmc.wordify.resources.theme.WordifyTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository: WordRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WordifyTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    TestComp(repo = repository)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WordifyTheme {
        Greeting("Android")
    }
}

@Composable
fun TestComp(
    repo: WordRepository,
) {
    LaunchedEffect(Unit) {
        launch {
            repo.getWordById("track")
            repo.getWordById("make")
            repo.getWordById("house")
            repo.getWordById("luck")
            repo.getWordById("step")
        }
    }

    val lazyPages = repo.getAllWords(Sort.ASC_NAME)
        .collectAsLazyPagingItems()

    LazyColumn {
        items(lazyPages) { word ->
            Text(text = word?.wordId ?: "No word")
        }
    }
}










