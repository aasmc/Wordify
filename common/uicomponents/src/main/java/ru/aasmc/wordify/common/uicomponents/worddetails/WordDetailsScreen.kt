package ru.aasmc.wordify.common.uicomponents.worddetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.aasmc.constants.WordDetailsTestTags
import ru.aasmc.wordify.common.core.presentation.model.UISyllable
import ru.aasmc.wordify.common.core.presentation.model.UIWord
import ru.aasmc.wordify.common.core.presentation.model.UIWordProperties
import ru.aasmc.wordify.common.core.utils.ProgressBarState.LOADING
import ru.aasmc.wordify.common.resources.R
import ru.aasmc.wordify.common.uicomponents.extensions.SwipeDismissSnackBarHost

@Composable
fun WordDetailsScreen(
    viewModel: WordDetailsViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val viewState by viewModel.viewState.collectAsState()
    viewState.errorReason?.let { errorEvent ->
        val unhandledError = errorEvent.getContentIfNotHandled() ?: return@let
        val errorMessage = stringResource(id = R.string.no_such_word_error)
        LaunchedEffect(key1 = unhandledError) {
            scaffoldState.snackbarHostState.showSnackbar(
                errorMessage
            )
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { snackbarHostState ->
            SwipeDismissSnackBarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )
        }
    ) {
        WordDetailsInternal(
            viewState = viewState,
            viewModel = viewModel
        )
    }
}

@Composable
private fun WordDetailsInternal(
    viewState: WordDetailsViewState,
    viewModel: WordDetailsViewModel
) {
    if (viewState.progressBarState == LOADING) {
        WordLoading()
    } else {
        val uiWord = viewState.uiWord
        if (uiWord == null) {
            WordError(
                stringResource(id = R.string.no_such_word_error)
            )
        } else {
            var isFavourite by remember {
                mutableStateOf(uiWord.isFavourite)
            }
            WordSuccess(
                uiWord = uiWord,
                onFavouriteClick = {
                    viewModel.handleEvent(
                        WordDetailsEvent.SetWordFavourite(
                            !uiWord.isFavourite, uiWord.wordId
                        )
                    )
                    isFavourite = !uiWord.isFavourite
                },
                isFavourite = isFavourite,
            )
        }
    }
}

@Composable
private fun WordSuccess(
    uiWord: UIWord,
    isFavourite: Boolean,
    onFavouriteClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = uiWord.wordName,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .weight(1f),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
            )
            val imageVector =
                if (isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
            Icon(
                imageVector = imageVector,
                tint = MaterialTheme.colors.primary,
                contentDescription = stringResource(id = R.string.fav_icon_description),
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false),
                        onClick = {
                            onFavouriteClick()
                        }
                    )
            )
        }
        if (uiWord.pronunciation.isNotEmpty()) {
            PropertyRow(
                label = stringResource(id = R.string.label_pronunciation),
                property = uiWord.pronunciation,
            )
        }
        // syllable string always contains 4 symbols  [  ]
        if (uiWord.syllable.syllableList.length > 4) {
            SyllableRow(
                syllable = uiWord.syllable,
            )
        }
        WordDivider()

        if (uiWord.wordProperties.isEmpty()) {
            WordError(
                stringResource(id = R.string.no_word_definition)
            )
        }
        uiWord.wordProperties.forEachIndexed { index, props ->
            WordPropertiesRow(
                wordProperties = props,
            )
            if (index != uiWord.wordProperties.lastIndex) {
                WordDivider()
            }
        }
    }
}

@Composable
private fun WordDivider() {
    Divider(
        thickness = 1.dp,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun SyllableRow(
    syllable: UISyllable,
) {
    PropertyRow(
        label = stringResource(id = R.string.label_syllables),
        property = syllable.syllableList,
    )
}

@Composable
private fun WordPropertiesRow(
    wordProperties: UIWordProperties,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(WordDetailsTestTags.WORD_PROPERTY_ROW_TAG)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            if (wordProperties.partOfSpeech.isNotEmpty()) {
                PropertyRow(
                    label = stringResource(id = R.string.label_part_of_speech),
                    property = wordProperties.partOfSpeech,
                )
            }
            if (wordProperties.definition.isNotEmpty()) {
                PropertyRow(
                    label = stringResource(id = R.string.label_definition),
                    property = wordProperties.definition,
                )
            }
            if (wordProperties.synonyms.isNotEmpty()) {
                ListPropertyColumn(
                    title = stringResource(id = R.string.label_synonyms),
                    properties = wordProperties.synonyms,
                )
            }
            if (wordProperties.derivation.isNotEmpty()) {
                ListPropertyColumn(
                    title = stringResource(id = R.string.label_derivation),
                    properties = wordProperties.derivation,
                )
            }
            if (wordProperties.examples.isNotEmpty()) {
                ListPropertyColumn(
                    title = stringResource(id = R.string.label_examples),
                    properties = wordProperties.examples,
                )
            }
        }
    }
}

@Composable
private fun ListPropertyColumn(
    title: String,
    properties: List<String>,
) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        val color = if (MaterialTheme.colors.isLight) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.primary.copy(alpha = 0.8f)
        }
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            style = MaterialTheme.typography.subtitle1,
            color = color,
            fontStyle = FontStyle.Italic
        )
        properties.forEachIndexed { index, prop ->
            Text(
                text = "${index + 1}. $prop",
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                style = MaterialTheme.typography.subtitle1,
            )
        }
    }
}

@Composable
private fun PropertyRow(
    label: String,
    property: String,
) {
    val color = if (MaterialTheme.colors.isLight) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.primary.copy(alpha = 0.8f)
    }
    val text = buildAnnotatedString {
        append(
            AnnotatedString(
                label,
                spanStyle = SpanStyle(
                    fontStyle = FontStyle.Italic,
                    color = color,
                )
            )
        )
        append(
            AnnotatedString(
                " $property",
                spanStyle = SpanStyle(
                    color = MaterialTheme.colors.onSurface
                )
            )
        )
    }
    SelectionContainer {
        Text(
            text = text,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.subtitle1,
        )
    }
}

@Composable
private fun WordError(
    message: String
) {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun WordLoading() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .testTag(WordDetailsTestTags.WORD_LOADING_TAG),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.onSurface
        )
    }
}

















