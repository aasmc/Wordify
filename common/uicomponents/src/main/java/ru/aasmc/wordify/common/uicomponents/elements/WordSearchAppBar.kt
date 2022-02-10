package ru.aasmc.wordify.common.uicomponents.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import ru.aasmc.wordify.common.resources.R
import ru.aasmc.wordify.resources.theme.WordifyTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WordSearchToolbar(
    onQueryChanged: (String) -> Unit,
    onExecuteSearch: () -> Unit,
    onSearchStarted: (Boolean) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var currentJob by remember {
        mutableStateOf<Job?>(null)
    }
    val scope = rememberCoroutineScope()
    var query by remember {
        mutableStateOf("")
    }
    val searchStarted = query.isNotEmpty()
    onSearchStarted(searchStarted)
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colors.background,
        elevation = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = query,
                onValueChange = {
                    query = it
                    onQueryChanged(query)
                },
                modifier = Modifier
                    .fillMaxWidth(.9f)
                    .padding(8.dp),
                label = { Text(text = stringResource(id = R.string.search_label)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onExecuteSearch()
                        keyboardController?.hide()
                        onSearchStarted(false)
                        query = ""
                    }
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_icon_description)
                    )
                },
                textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background
                )
            )
        }
    }
}


@Preview
@Composable
private fun WordSearchToolbarDarkThemePreview() {
    WordifyTheme(darkTheme = true) {
        WordSearchToolbar(
            {},
            {},
            {}
        )
    }
}

@Preview
@Composable
private fun WordSearchToolbarLightThemePreview() {
    WordifyTheme(darkTheme = false) {
        WordSearchToolbar(
            {},
            {},
            {}
        )
    }
}














