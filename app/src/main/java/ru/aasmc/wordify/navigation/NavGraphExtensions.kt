package ru.aasmc.wordify.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.aasmc.constants.WordConstants
import ru.aasmc.wordify.common.uicomponents.worddetails.WordDetailsScreen
import ru.aasmc.wordify.features.settings.presentation.SettingsScreen
import ru.aasmc.wordify.features.wordfavouriteslist.presentation.WordFavListScreen
import ru.aasmc.wordify.features.wordlist.presentation.WordListScreen

fun NavGraphBuilder.addWordDetailsScreen() {
    composable(
        route = "${Screen.WordDetailsScreen.route}/{${WordConstants.WORD_ID_ARGUMENT}}",
        arguments = Screen.WordDetailsScreen.arguments,
    ) {
        WordDetailsScreen()
    }
}

fun NavGraphBuilder.addFavWordDetailsScreen() {
    composable(
        route = "${Screen.FavWordDetailsScreen.route}/{${WordConstants.WORD_ID_ARGUMENT}}",
        arguments = Screen.FavWordDetailsScreen.arguments,
    ) {
        WordDetailsScreen()
    }
}

fun NavGraphBuilder.addWordListScreen(
    onWordClick: (String) -> Unit,
) {
    navigation(
        route = Screen.WordListScreen.baseRoot,
        startDestination = Screen.WordListScreen.route
    ) {
        composable(
            route = Screen.WordListScreen.route,
        ) {
            WordListScreen(
                onWordClick = onWordClick,
            )
        }
        addWordDetailsScreen()
    }
}

fun NavGraphBuilder.addFavWordListScreen(
    onWordClick: (String) -> Unit,
) {
    navigation(
        route = Screen.FavWordsScreen.baseRoot,
        startDestination = Screen.FavWordsScreen.route
    ) {
        composable(route = Screen.FavWordsScreen.route) {
            WordFavListScreen(
                onWordClick = onWordClick
            )
        }
        addFavWordDetailsScreen()
    }
}

fun NavGraphBuilder.addSettingsScreen() {
    composable(
        route = Screen.SettingsScreen.baseRoot,
    ) {
        SettingsScreen(
            title = stringResource(id = Screen.SettingsScreen.resourceId),
        )
    }
}