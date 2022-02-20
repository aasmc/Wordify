package ru.aasmc.wordify.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import ru.aasmc.wordify.navigation.*
import ru.aasmc.wordify.resources.theme.WordifyTheme

@Composable
fun MainScreen(
    appTheme: ThemePreference,
    screens: List<Screen>
) {
    WordifyTheme(darkTheme = appTheme.shouldUseDarkTheme(isSystemInDarkTheme())) {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomNav(navController, screens)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.WordListScreen.baseRoot,
                modifier = Modifier.padding(innerPadding)
            ) {
                addWordListScreen(
                    onWordClick = { name ->
                        navController.navigate(
                            "${Screen.WordDetailsScreen.route}/$name",
                        )
                    },
                )
                addFavWordListScreen(
                    onWordClick = { name ->
                        navController.navigate(
                            "${Screen.FavWordDetailsScreen.route}/$name"
                        )
                    },
                )
                addSettingsScreen()
            }
        }
    }
}