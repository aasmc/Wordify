package ru.aasmc.wordify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import ru.aasmc.wordify.common.core.domain.model.UserPreferences
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import ru.aasmc.wordify.common.uicomponents.rememberFlowWithLifecycle
import ru.aasmc.wordify.features.settings.domain.usecases.GetAppThemeFlow
import ru.aasmc.wordify.features.settings.presentation.PreferencesViewModel
import ru.aasmc.wordify.features.settings.presentation.SettingsScreen
import ru.aasmc.wordify.resources.theme.WordifyTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository: WordRepository

    @Inject
    lateinit var getAppThemeFlow: GetAppThemeFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val appTheme by getAppThemeFlow()
                .collectAsState(ThemePreference.AUTO_THEME)

            WordifyTheme(darkTheme = appTheme.shouldUseDarkTheme(isSystemInDarkTheme())) {
//                // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
//                    TestComp(repo = repository)
//                }
                val vm: PreferencesViewModel = hiltViewModel()
                SettingsScreen(
                    title = "Settings",
                    viewModel = vm,
                    appTheme = appTheme
                )
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
    WordifyTheme(darkTheme = true) {
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










