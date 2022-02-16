package ru.aasmc.wordify.settings

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.aasmc.constants.SettingsTestTags
import ru.aasmc.wordify.common.core.di.PreferencesModule
import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import ru.aasmc.wordify.common.core.domain.usecases.GetSortOrderFlow
import ru.aasmc.wordify.common.core.fakes.FakePreferencesRepository
import ru.aasmc.wordify.common.resources.R
import ru.aasmc.wordify.features.settings.domain.usecases.ChangeAppTheme
import ru.aasmc.wordify.features.settings.domain.usecases.ChangeWordSortOrder
import ru.aasmc.wordify.features.settings.domain.usecases.GetAppThemeFlow
import ru.aasmc.wordify.features.settings.presentation.PreferencesViewModel
import ru.aasmc.wordify.features.settings.presentation.SettingsScreen
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidTest
@UninstallModules(PreferencesModule::class)
class SettingsScreenTest {

    @get:Rule(order = 0)
    val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var prefRepository: PreferencesRepository

    private lateinit var prefViewModel: PreferencesViewModel

    @Before
    fun setup() {
        hiltTestRule.inject()
        prefViewModel = PreferencesViewModel(
            changeAppTheme = ChangeAppTheme(prefRepository),
            changeWordSortOrder = ChangeWordSortOrder(prefRepository),
            getSortOrderFlow = GetSortOrderFlow(prefRepository),
            getAppThemeFlow = GetAppThemeFlow(prefRepository)
        )
        composeTestRule.setContent {
            SettingsScreen(
                title = stringResource(id = R.string.screen_settings),
                viewModel = prefViewModel
            )
        }
    }

    @Test
    fun initially_darkTheme_ascNameSortOrder() {
        composeTestRule.onAllNodesWithTag(
            SettingsTestTags.RADIO_BUTTON_TAG,
            useUnmergedTree = true
        ).assertAny(
            hasAnySibling(
                hasText("a -> z")
            ) and hasParent(
                isSelected()
            )
        )

        composeTestRule.onAllNodesWithTag(
            SettingsTestTags.RADIO_BUTTON_TAG,
            useUnmergedTree = true
        ).assertAny(
            hasAnySibling(
                hasText("System theme")
            ) and hasParent(
                isSelected()
            )
        )
    }

    @Test
    fun changeSortOrder_correctlyChangesUI() {
        composeTestRule.onAllNodesWithTag(
            SettingsTestTags.RADIO_BUTTON_TAG,
            useUnmergedTree = true
        ).filter(
            hasAnySibling(
                hasText("z -> a")
            )
        ).onFirst().performClick()

        composeTestRule.onAllNodesWithTag(
            SettingsTestTags.RADIO_BUTTON_TAG,
            useUnmergedTree = true
        ).assertAny(
            hasAnySibling(
                hasText("z -> a")
            ) and hasParent(
                isSelected()
            )
        )
    }

    @Test
    fun changeAppTheme_correctlyChangesUI() {
        composeTestRule.onAllNodesWithTag(
            SettingsTestTags.RADIO_BUTTON_TAG,
            useUnmergedTree = true
        ).filter(
            hasAnySibling(
                hasText("Dark theme")
            )
        ).onFirst().performClick()

        composeTestRule.onAllNodesWithTag(
            SettingsTestTags.RADIO_BUTTON_TAG,
            useUnmergedTree = true
        ).assertAny(
            hasAnySibling(
                hasText("Dark theme")
            ) and hasParent(
                isSelected()
            )
        )
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class TestPreferencesModule {
        @Binds
        @Singleton
        abstract fun bindPreferences(
            preferences: FakePreferencesRepository
        ): PreferencesRepository

    }

}