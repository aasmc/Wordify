package ru.aasmc.wordify.navigation

import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.aasmc.constants.WordConstants
import ru.aasmc.wordify.common.resources.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val arguments: List<NamedNavArgument>
) {
    object WordListScreen : Screen(
        route = "wordList",
        resourceId = R.string.screen_home,
        arguments = emptyList()
    )

    object FavWordsScreen : Screen(
        route = "favWords",
        resourceId = R.string.screen_favourite,
        arguments = emptyList()
    )

    object SettingsScreen : Screen(
        route = "settingsScreen",
        resourceId = R.string.screen_settings,
        arguments = emptyList()
    )

    object WordDetailsScreen : Screen(
        route = "wordDetails",
        resourceId = R.string.screen_details,
        arguments = listOf(
            navArgument(name = WordConstants.WORD_ID_ARGUMENT) {
                type = NavType.StringType
            }
        )
    )
}
