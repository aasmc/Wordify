package ru.aasmc.wordify.navigation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.aasmc.constants.WordDetailsTestTags
import ru.aasmc.wordify.MainActivity
import ru.aasmc.wordify.common.core.di.ActivityRetainedModule
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import ru.aasmc.wordify.common.core.fakes.FakeWordifyRepository
import ru.aasmc.wordify.common.core.utils.CoroutineDispatchersProvider
import ru.aasmc.wordify.common.core.utils.DispatchersProvider
import ru.aasmc.wordify.presentation.MainScreen

@HiltAndroidTest
@UninstallModules(ActivityRetainedModule::class)
class NavigationTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val screens = listOf(
        Screen.WordListScreen,
        Screen.FavWordsScreen,
        Screen.SettingsScreen
    )

    @Before
    fun setup() {
        composeTestRule.setContent {
            MainScreen(appTheme = ThemePreference.LIGHT_THEME, screens = screens)
        }
    }

    @Test
    fun homeScreenTabSelected_onStartup() {
        composeTestRule.onNodeWithText(
            "Home"
        ).assertIsSelected()
    }

    @Test
    fun clickOnSettings_changesScreen() {
        composeTestRule.onNodeWithText("Settings")
            .performClick()

        composeTestRule.onNodeWithText("Sort order")
            .assertIsDisplayed()
    }

    @Test
    fun clickOnWord_navigatesToWordDetails() {
        composeTestRule.onNodeWithText("Word 1")
            .performClick()

        composeTestRule.onNodeWithTag(WordDetailsTestTags.WORD_PROPERTY_ROW_TAG)
            .assertIsDisplayed()
    }

    @Test
    fun clickOnWordAndBack_navigatesToWord_thenBackToRouteScreen() {
        composeTestRule.onNodeWithText("Word 1")
            .performClick()

        composeTestRule.onNodeWithTag(WordDetailsTestTags.WORD_PROPERTY_ROW_TAG)
            .assertIsDisplayed()

        Espresso.pressBack()
        composeTestRule.onNodeWithText("Word 1")
            .assertIsDisplayed()
    }

    @Test
    fun clickOnSettings_thenBack_HomeScreenIsDisplayed() {
        composeTestRule.onNodeWithText(
            "Home"
        ).assertIsSelected()
        composeTestRule.onNodeWithText("Settings")
            .performClick()

        composeTestRule.onNodeWithText("Sort order")
            .assertIsDisplayed()

        Espresso.pressBack()
        composeTestRule.onNodeWithText("Word 1")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(
            "Home"
        ).assertIsSelected()
    }


    @Module
    @InstallIn(ActivityRetainedComponent::class)
    abstract class TestActivityRetainedModule {
        @Binds
        @ActivityRetainedScoped
        abstract fun bindWordRepository(
            wordRepository: FakeWordifyRepository
        ): WordRepository

        @Binds
        abstract fun bindDispatchersProvider(
            dispatchersProvider: CoroutineDispatchersProvider
        ): DispatchersProvider
    }
}