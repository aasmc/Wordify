package ru.aasmc.wordify.worddetails

import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import ru.aasmc.constants.WordConstants
import ru.aasmc.constants.WordDetailsTestTags
import ru.aasmc.wordify.common.core.di.ActivityRetainedModule
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import ru.aasmc.wordify.common.core.domain.usecases.GetWordDetailsFlow
import ru.aasmc.wordify.common.core.domain.usecases.SetWordFavourite
import ru.aasmc.wordify.common.core.fakes.FakeWordifyRepository
import ru.aasmc.wordify.common.core.utils.CoroutineDispatchersProvider
import ru.aasmc.wordify.common.core.utils.DispatchersProvider
import ru.aasmc.wordify.common.uicomponents.worddetails.WordDetailsScreen
import ru.aasmc.wordify.common.uicomponents.worddetails.WordDetailsViewModel
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(ActivityRetainedModule::class)
class WordDetailsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private val savedStateHandle = SavedStateHandle()

    private lateinit var viewModel: WordDetailsViewModel

    @Inject
    lateinit var wordifyRepository: FakeWordifyRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    private fun prepareViewModel(wordName: String) {
        savedStateHandle.set(WordConstants.WORD_ID_ARGUMENT, wordName)
        viewModel = WordDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getWordByNameFlow = GetWordDetailsFlow(
                wordifyRepository
            ),
            setWordFavourite = SetWordFavourite(
                wordifyRepository
            )
        )
    }

    private fun createSuccessScreen(wordName: String) {
        prepareViewModel(wordName)
        wordifyRepository.success = true
        composeTestRule.setContent {
            WordDetailsScreen(
                viewModel = viewModel
            )
        }
    }

    @Test
    fun incorrectWord_errorMessageDisplayed() {
        prepareViewModel("Wrong Word")
        wordifyRepository.success = false
        composeTestRule.setContent {
            WordDetailsScreen(
                viewModel = viewModel
            )
        }

        composeTestRule.onAllNodesWithText(
            "Sorry there's no such word."
        ).assertCountEquals(2)
    }

    @Test
    fun correctWord_UI_correct() {
        createSuccessScreen("Word 1")

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("WORD_DETAILS_TEST")

        composeTestRule.onNodeWithText("Sorry there's no such word.")
            .assertDoesNotExist()

        composeTestRule.onNodeWithText("Word 1")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Pronunciation: [ 1 ]")
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Set word favourite icon")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Syllables: [ 1 ]")
            .assertIsDisplayed()

        composeTestRule.onAllNodesWithTag(
            WordDetailsTestTags.WORD_PROPERTY_ROW_TAG
        ).assertCountEquals(1)

        composeTestRule.onNodeWithText("Definition: 1")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Part of speech: 1")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Synonyms:")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Derivations:")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Examples:")
            .assertIsDisplayed()
    }

    @Test
    fun setFavouriteClicked_wordIsFavouriteChanges() {
        createSuccessScreen("Word 1")
        composeTestRule.onNodeWithContentDescription("Set word favourite icon")
            .performClick()
        composeTestRule.onNodeWithContentDescription("Set word not favourite icon")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Set word favourite icon")
            .assertDoesNotExist()
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



























