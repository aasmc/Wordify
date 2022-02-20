package ru.aasmc.wordify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import dagger.hilt.android.AndroidEntryPoint
import ru.aasmc.constants.WordConstants
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import ru.aasmc.wordify.common.uicomponents.worddetails.WordDetailsScreen
import ru.aasmc.wordify.features.settings.domain.usecases.GetAppThemeFlow
import ru.aasmc.wordify.features.settings.presentation.SettingsScreen
import ru.aasmc.wordify.features.wordfavouriteslist.presentation.WordFavListScreen
import ru.aasmc.wordify.features.wordlist.presentation.WordListScreen
import ru.aasmc.wordify.navigation.*
import ru.aasmc.wordify.presentation.MainScreen
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

    @Composable
    private fun MainContent(screens: List<Screen>) {
        val appTheme by getAppThemeFlow()
            .collectAsState()

        MainScreen(appTheme, screens)
    }
}
