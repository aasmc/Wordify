package ru.aasmc.wordify.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.aasmc.constants.WordConstants
import ru.aasmc.wordify.common.resources.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val arguments: List<NamedNavArgument>,
    val imageVector: ImageVector,
    val baseRoot: String
) {
    object WordListScreen : Screen(
        route = "wordList",
        resourceId = R.string.screen_home,
        arguments = emptyList(),
        imageVector = Icons.Filled.Home,
        baseRoot = "wordListRoot/"
    )

    object FavWordsScreen : Screen(
        route = "favWords",
        resourceId = R.string.screen_favourite,
        arguments = emptyList(),
        imageVector = Icons.Filled.Favorite,
        baseRoot = "favWordListRoot/"
    )

    object SettingsScreen : Screen(
        route = "settingsScreen",
        resourceId = R.string.screen_settings,
        arguments = emptyList(),
        imageVector = Icons.Filled.Settings,
        baseRoot = "settingsRoot/"
    )

    object WordDetailsScreen : Screen(
        route = "wordDetails",
        resourceId = R.string.screen_details,
        arguments = listOf(
            navArgument(name = WordConstants.WORD_ID_ARGUMENT) {
                type = NavType.StringType
            }
        ),
        imageVector = Icons.Default.Done, // ignored
        baseRoot = "wordDetailsRoot/" // ignored
    )

    object FavWordDetailsScreen : Screen(
        route = "favWordDetails",
        resourceId = R.string.screen_fav_details,
        arguments = listOf(
            navArgument(name = WordConstants.WORD_ID_ARGUMENT) {
                type = NavType.StringType
            }
        ),
        imageVector = Icons.Default.Done, // ignored
        baseRoot = "favWordDetailsRoot/" // ignored
    )
}
