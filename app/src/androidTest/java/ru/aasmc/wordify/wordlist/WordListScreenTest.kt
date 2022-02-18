package ru.aasmc.wordify.wordlist

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.input.ImeAction
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.aasmc.constants.SearchConstants
import ru.aasmc.constants.WordConstants
import ru.aasmc.constants.WordListConstants
import ru.aasmc.wordify.common.core.di.ActivityRetainedModule
import ru.aasmc.wordify.common.core.di.PreferencesModule
import ru.aasmc.wordify.common.core.domain.repositories.PreferencesRepository
import ru.aasmc.wordify.common.core.domain.repositories.WordRepository
import ru.aasmc.wordify.common.core.domain.usecases.GetSortOrderFlow
import ru.aasmc.wordify.common.core.domain.usecases.SaveRecentlySearchedWord
import ru.aasmc.wordify.common.core.domain.usecases.SearchRecentlySearchedWords
import ru.aasmc.wordify.common.core.domain.usecases.SetWordFavourite
import ru.aasmc.wordify.common.core.fakes.FakePreferencesRepository
import ru.aasmc.wordify.common.core.fakes.FakeWordifyRepository
import ru.aasmc.wordify.common.core.utils.CoroutineDispatchersProvider
import ru.aasmc.wordify.common.core.utils.DispatchersProvider
import ru.aasmc.wordify.features.wordlist.domain.usecases.GetWordsList
import ru.aasmc.wordify.features.wordlist.presentation.WordListScreen
import ru.aasmc.wordify.features.wordlist.presentation.WordListViewModel
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidTest
@UninstallModules(ActivityRetainedModule::class, PreferencesModule::class)
class WordListScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: WordListViewModel

    @Inject
    lateinit var repository: FakeWordifyRepository

    @Inject
    lateinit var preferencesRepository: FakePreferencesRepository

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = WordListViewModel(
            getWordsList = GetWordsList(repository),
            setWordFavourite = SetWordFavourite(repository),
            searchRecentlySearchedWords = SearchRecentlySearchedWords(repository),
            saveRecentlySearchedWord = SaveRecentlySearchedWord(repository),
            getSortOrderStateFlow = GetSortOrderFlow(preferencesRepository)
        )
    }

    @After
    fun refreshRepo() {
        repository.refresh()
    }

    @Test
    fun no_words_displays_correct_message() {
        repository.clearWords()
        composeTestRule.setContent {
            WordListScreen(onWordClick = {}, wordListViewModel = viewModel)
        }
        composeTestRule.onNodeWithText("You have not searched for words yet.")
            .assertIsDisplayed()

    }

    @Test
    fun five_words_in_repo_correct_UI() {
        composeTestRule.setContent {
            WordListScreen(onWordClick = {}, wordListViewModel = viewModel)
        }
        composeTestRule.onAllNodes(
            hasTestTag(WordListConstants.WORD_ITEM_CARD_TAD)
        ).assertCountEquals(5)

        composeTestRule.onAllNodesWithTag(
            WordListConstants.WORD_ITEM_CARD_TAD,
            useUnmergedTree = true
        )
            .onFirst()
            .assert(
                hasAnyChild(
                    hasTestTag(WordConstants.WORD_TITLE_TAG)
                            and hasText("Word 1")
                )
            )

        composeTestRule.onAllNodesWithTag(
            WordListConstants.WORD_ITEM_CARD_TAD,
            useUnmergedTree = true
        )
            .onLast()
            .assert(
                hasAnyChild(
                    hasTestTag(WordConstants.WORD_TITLE_TAG)
                            and hasText("Word 5")
                )
            )
    }

    @Test
    fun search_query_no_previous_searches_empty_recently_searched_list() {
        composeTestRule.setContent {
            WordListScreen(onWordClick = {}, wordListViewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Search")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Search")
            .performTextInput("Word")

        composeTestRule.onAllNodesWithTag(SearchConstants.RECENTLY_SEARCHED_WORD_TAG)
            .assertCountEquals(0)
    }

    @Test
    fun search_query_has_previous_searches_correct_recently_searched_list() {
        composeTestRule.setContent {
            WordListScreen(onWordClick = {}, wordListViewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Search")
            .performTextInput("Word")

        composeTestRule.onNode(
            hasImeAction(ImeAction.Done)
        ).performImeAction()

        composeTestRule.onNodeWithText("Search")
            .performTextInput("Wo")

        composeTestRule.onAllNodesWithTag(SearchConstants.RECENTLY_SEARCHED_WORD_TAG)
            .assertCountEquals(1)
    }

    @Test
    fun initially_all_words_are_not_favourite() {
        composeTestRule.setContent {
            WordListScreen(onWordClick = {}, wordListViewModel = viewModel)
        }

        composeTestRule.onAllNodesWithContentDescription("Set word favourite icon")
            .assertCountEquals(5)
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











