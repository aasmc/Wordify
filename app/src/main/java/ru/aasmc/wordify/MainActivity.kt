package ru.aasmc.wordify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import ru.aasmc.wordify.features.settings.domain.usecases.GetAppThemeFlow
import ru.aasmc.wordify.features.settings.presentation.SettingsScreen
import ru.aasmc.wordify.features.wordfavouriteslist.presentation.WordFavListScreen
import ru.aasmc.wordify.features.wordlist.presentation.WordListScreen
import ru.aasmc.wordify.navigation.Screen
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

        val screens = listOf(
            Screen.WordListScreen,
            Screen.FavWordsScreen,
            Screen.SettingsScreen
        )
        setContent {
            val appTheme by getAppThemeFlow()
                .collectAsState()

            WordifyTheme(darkTheme = appTheme.shouldUseDarkTheme(isSystemInDarkTheme())) {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BottomNavigation {
                            val navBackStackEntry
                                    by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            screens.forEach { screen ->
                                val imageVector = when (screen) {
                                    Screen.FavWordsScreen -> Icons.Filled.Favorite
                                    Screen.SettingsScreen -> Icons.Filled.Settings
                                    // this will be ignored in bottom navigation
                                    Screen.WordDetailsScreen -> Icons.Filled.Edit
                                    Screen.WordListScreen -> Icons.Filled.Home
                                }
                                BottomNavigationItem(
                                    icon = {
                                        Icon(
                                            imageVector = imageVector,
                                            contentDescription = stringResource(
                                                id = screen.resourceId
                                            )
                                        )
                                    },
                                    label = {
                                        Text(text = stringResource(id = screen.resourceId))
                                    },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }

                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.WordListScreen.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        addWordListScreen(
                            onExecuteSearch = { name ->
                                lifecycleScope.launch {
                                    repository.getWordByName(name)
                                }
                            },
                            onWordClick = {}
                        )
                        addFavWordListScreen(
                            onExecuteSearch = { name ->
                                lifecycleScope.launch {
                                    repository.getWordByName(name)
                                }
                            },
                            onWordClick = {}
                        )
                        addSettingsScreen(
                            theme = appTheme,
                        )
                    }
                }
            }
        }
    }
}

private fun NavGraphBuilder.addWordListScreen(
    onExecuteSearch: (String) -> Unit,
    onWordClick: (Long) -> Unit
) {
    composable(Screen.WordListScreen.route) {
        WordListScreen(
            onExecuteSearch = onExecuteSearch,
            onWordClick = onWordClick
        )
    }
}

private fun NavGraphBuilder.addFavWordListScreen(
    onExecuteSearch: (String) -> Unit,
    onWordClick: (Long) -> Unit
) {
    composable(Screen.FavWordsScreen.route) {
        WordFavListScreen(
            onExecuteSearch = onExecuteSearch,
            onWordClick = onWordClick
        )
    }
}

private fun NavGraphBuilder.addSettingsScreen(
    theme: ThemePreference,
) {
    composable(Screen.SettingsScreen.route) {
        SettingsScreen(
            title = stringResource(id = Screen.SettingsScreen.resourceId),
            appTheme = theme,
        )
    }
}









