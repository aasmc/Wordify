package ru.aasmc.wordify.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.aasmc.constants.WordConstants

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument>
) {
    object WordListScreen : Screen(
        route = "wordList",
        arguments = emptyList()
    )

    object FavWordsScreen : Screen(
        route = "favWords",
        arguments = emptyList()
    )

    object WordDetailsScreen : Screen(
        route = "wordDetails",
        arguments = listOf(
            navArgument(name = WordConstants.WORD_ID_ARGUMENT) {
                type = NavType.StringType
            }
        )
    )
}
