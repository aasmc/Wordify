package ru.aasmc.wordify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import dagger.hilt.android.AndroidEntryPoint
import ru.aasmc.constants.WordConstants
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import ru.aasmc.wordify.common.uicomponents.worddetails.WordDetailsScreen
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
    lateinit var getAppThemeFlow: GetAppThemeFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screens = listOf(
            Screen.WordListScreen,
            Screen.FavWordsScreen,
            Screen.SettingsScreen
        )
        setContent {
            MainContent(screens)
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun MainContent(screens: List<Screen>) {
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
                                Screen.WordListScreen -> Icons.Filled.Home
                                // this will be ignored in bottom navigation
                                Screen.WordDetailsScreen -> Icons.Filled.Edit
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
                                    if (screen.route != currentDestination?.route) {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
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
                        onWordClick = { name ->
                            navController.navigate(
                                "${Screen.WordDetailsScreen.route}/$name"
                            )
                        },
                    )
                    addFavWordListScreen(
                        onWordClick = { name ->
                            navController.navigate(
                                "${Screen.WordDetailsScreen.route}/$name"
                            )
                        },
                    )
                    addSettingsScreen(
                        theme = appTheme,
                    )
                    addWordDetailsScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addWordDetailsScreen() {
    composable(
        route = "${Screen.WordDetailsScreen.route}/{${WordConstants.WORD_ID_ARGUMENT}}",
        arguments = Screen.WordDetailsScreen.arguments,
    ) {
        WordDetailsScreen()
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addWordListScreen(
    onWordClick: (String) -> Unit,
) {
    composable(
        route = Screen.WordListScreen.route,
    ) {
        WordListScreen(
            onWordClick = onWordClick
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addFavWordListScreen(
    onWordClick: (String) -> Unit,
) {
    composable(
        route = Screen.FavWordsScreen.route,
    ) {
        WordFavListScreen(
            onWordClick = onWordClick
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addSettingsScreen(
    theme: ThemePreference,
) {
    composable(
        route = Screen.SettingsScreen.route,
    ) {
        SettingsScreen(
            title = stringResource(id = Screen.SettingsScreen.resourceId),
            appTheme = theme,
        )
    }
}









