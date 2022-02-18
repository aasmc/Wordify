package ru.aasmc.wordify.common.uicomponents.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.aasmc.constants.WordConstants
import ru.aasmc.wordify.common.resources.R
import ru.aasmc.wordify.resources.theme.WordifyTheme

@Composable
fun WordItemCard(
    wordName: String,
    partOfSpeech: String,
    pronunciation: String,
    description: String,
    isFavourite: Boolean,
    onChangeFavourite: () -> Unit,
    onWordItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onWordItemClick()
            },
        shape = MaterialTheme.shapes.large,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            WordItemTitle(title = wordName)
            WordCharacteristics(
                partOfSpeech = partOfSpeech,
                pronunciation = pronunciation,
                isFavourite = isFavourite,
                onFavouriteClick = onChangeFavourite
            )
            WordDescription(description = description)
        }
    }
}

@Preview
@Composable
private fun WordItemCardDarkTheme() {
    WordifyTheme(darkTheme = true) {
        WordItemCard(
            "Track",
            "noun",
            "[ træk ]",
            "a bar or pair of parallel bars of rolled steel making the railway along which railroad cars or other vehicles can roll.",
            true,
            {},
            {}
        )
    }
}

@Preview
@Composable
private fun WordItemCardLightTheme() {
    WordifyTheme(darkTheme = false) {
        WordItemCard(
            "Track",
            "noun",
            "[ træk ]",
            "a bar or pair of parallel bars of rolled steel making the railway along which railroad cars or other vehicles can roll.",
            false,
            {},
            {}
        )
    }
}

@Composable
private fun WordDescription(
    description: String
) {
    val text = description.ifEmpty {
        stringResource(id = R.string.no_definition_available)
    }
    Text(
        text = "$text.",
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.Justify,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun WordItemTitle(
    title: String
) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(WordConstants.WORD_TITLE_TAG),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.onSurface
    )
}

@Composable
private fun WordCharacteristics(
    partOfSpeech: String,
    pronunciation: String,
    isFavourite: Boolean,
    onFavouriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            if (partOfSpeech.isNotEmpty()) {
                Text(
                    text = "Part of speech: $partOfSpeech",
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.secondary,
                    fontStyle = FontStyle.Italic
                )
            }
            Text(
                text = "Pronunciation: $pronunciation",
                modifier = Modifier
                    .padding(bottom = 8.dp),
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.primary
            )
        }
        val imageVector =
            if (isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
        val contentDescription = if (isFavourite) {
            stringResource(id = R.string.set_not_fav_icon_description)
        } else {
            stringResource(id = R.string.set_fav_icon_description)
        }
        Icon(
            imageVector = imageVector,
            tint = MaterialTheme.colors.primary,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(32.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = {
                        onFavouriteClick()
                    }
                )
        )
    }
}

@Preview
@Composable
private fun WordCharacteristicsDarkThemeFavouritePreview() {
    WordifyTheme(darkTheme = true) {
        WordCharacteristics(
            "Part of speech: noun",
            "Pronunciation: [noun]",
            true,
            {}
        )
    }
}

@Preview
@Composable
private fun WordCharacteristicsLightThemeNotFavouritePreview() {
    WordifyTheme(darkTheme = false) {
        WordCharacteristics(
            "Part of speech: noun",
            "Pronunciation: [noun]",
            false,
            {}
        )
    }
}




















