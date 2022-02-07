package ru.aasmc.wordify.features.settings.presentation

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.aasmc.wordify.common.core.domain.model.UserPreferences
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import ru.aasmc.wordify.features.settings.R
import ru.aasmc.wordify.resources.theme.WordifyTheme

@Composable
fun SettingsScreen(
    title: String,
    appTheme: ThemePreference,
    viewModel: PreferencesViewModel
) {
    val sortFilters = listOf(
        stringResource(id = R.string.sort_name_asc),
        stringResource(id = R.string.sort_name_desc),
        stringResource(id = R.string.sort_time_added_asc),
        stringResource(id = R.string.sort_time_added_desc),
    )
    val themeFilters = listOf(
        stringResource(id = R.string.theme_auto),
        stringResource(id = R.string.theme_dark),
        stringResource(id = R.string.theme_light),
    )

    val ctx = LocalContext.current

    val sortOrder by viewModel.sortOrder.collectAsState()

    val selectedSort =
        getSelectedSort(sort = sortOrder, context = ctx)

    val selectedTheme =
        getSelectedTheme(themePreference = appTheme, context = ctx)

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        bottom = 16.dp
                    )
            )
            // sort order
            SettingsCard(
                label = stringResource(id = R.string.sort_order_title),
                settingsValues = sortFilters,
                selectedValue = selectedSort,
                onSelectedValueChange = { sortStr ->
                    viewModel.handleEvent(
                        UserPrefsEvent.ChangeSortOrder(
                            sortOrder = getSortOrderFromString(sortStr, ctx)
                        )
                    )
                },
            )
            // theme selection
            SettingsCard(
                label = stringResource(id = R.string.theme_selection_title),
                settingsValues = themeFilters,
                selectedValue = selectedTheme,
                onSelectedValueChange = { themeStr ->
                    viewModel.handleEvent(
                        UserPrefsEvent.ChangeTheme(
                            themePreference = getThemePrefFromString(themeStr, ctx)
                        )
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreviewDark() {
    val vm: PreferencesViewModel = viewModel()
    WordifyTheme(darkTheme = true) {
        SettingsScreen(
            title = "Settings",
            appTheme = ThemePreference.AUTO_THEME,
            viewModel = vm,
        )
    }
}

private fun getSortOrderFromString(sortStr: String, context: Context): Sort {
    return when (sortStr) {
        context.getString(R.string.sort_name_asc) -> Sort.ASC_NAME
        context.getString(R.string.sort_name_desc) -> Sort.DESC_NAME
        context.getString(R.string.sort_time_added_asc) -> Sort.ASC_TIME
        context.getString(R.string.sort_time_added_desc) -> Sort.DESC_TIME
        else -> throw IllegalArgumentException("This type of sort order is not supported: $sortStr")
    }
}

private fun getThemePrefFromString(themeStr: String, context: Context): ThemePreference {
    return when (themeStr) {
        context.getString(R.string.theme_auto) -> ThemePreference.AUTO_THEME
        context.getString(R.string.theme_dark) -> ThemePreference.DARK_THEME
        context.getString(R.string.theme_light) -> ThemePreference.LIGHT_THEME
        else -> throw IllegalArgumentException("This type of theme selection is not supported: $themeStr")
    }
}

private fun getSelectedSort(sort: Sort, context: Context): String {
    return when (sort) {
        Sort.ASC_NAME -> context.getString(R.string.sort_name_asc)
        Sort.DESC_NAME -> context.getString(R.string.sort_name_desc)
        Sort.ASC_TIME -> context.getString(R.string.sort_time_added_asc)
        Sort.DESC_TIME -> context.getString(R.string.sort_time_added_desc)
    }
}

private fun getSelectedTheme(themePreference: ThemePreference, context: Context): String {
    return when (themePreference) {
        ThemePreference.DARK_THEME -> context.getString(R.string.theme_dark)
        ThemePreference.LIGHT_THEME -> context.getString(R.string.theme_light)
        ThemePreference.AUTO_THEME -> context.getString(R.string.theme_auto)
    }
}

@Composable
private fun SettingsCard(
    label: String,
    settingsValues: List<String>,
    selectedValue: String,
    onSelectedValueChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.large,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
            SettingsColumn(
                settingsValues = settingsValues,
                selectedValue = selectedValue,
                onSelectedValueChange = onSelectedValueChange
            )
        }
    }
}

@Preview
@Composable
private fun SettingsCardPreviewDarkTheme() {
    WordifyTheme(darkTheme = true) {
        SettingsCard(
            label = "Sort Order",
            settingsValues = listOf("a -> z", "z -> a", "Time added asc", "Time added desc"),
            selectedValue = "a -> z",
            onSelectedValueChange = {}
        )
    }
}

@Preview
@Composable
private fun SettingsCardPreviewLightTheme() {
    WordifyTheme(darkTheme = false) {
        SettingsCard(
            label = "Sort Order",
            settingsValues = listOf("a -> z", "z -> a", "Time added asc", "Time added desc"),
            selectedValue = "a -> z",
            onSelectedValueChange = {}
        )
    }
}

@Composable
private fun SettingsColumn(
    settingsValues: List<String>,
    selectedValue: String,
    onSelectedValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        settingsValues.forEach { setting ->
            SettingsRow(
                isSelected = setting == selectedValue,
                settingsValue = setting,
                onSelectValue = onSelectedValueChange
            )
        }
    }
}

@Preview
@Composable
private fun SettingsColumnPreviewDarkTheme() {
    WordifyTheme(darkTheme = true) {
        SettingsColumn(
            settingsValues = listOf("a -> z", "z -> a"),
            selectedValue = "a -> z",
            onSelectedValueChange = {}
        )
    }
}

@Preview
@Composable
private fun SettingsColumnPreviewLightTheme() {
    WordifyTheme(darkTheme = false) {
        SettingsColumn(
            settingsValues = listOf("a -> z", "z -> a"),
            selectedValue = "a -> z",
            onSelectedValueChange = {}
        )
    }
}

@Composable
private fun SettingsRow(
    isSelected: Boolean,
    settingsValue: String,
    onSelectValue: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onSelectValue(settingsValue) },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = MaterialTheme.colors.secondary
            ),
            modifier = Modifier
                .padding(end = 8.dp, start = 8.dp)
        )
        Text(
            text = settingsValue,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(end = 8.dp, start = 8.dp)
        )
    }
}

@Preview
@Composable
private fun SelectedSettingsRowDarkThemePreview() {
    WordifyTheme(darkTheme = true) {
        SettingsRow(isSelected = true, settingsValue = "a -> z", onSelectValue = {})
    }
}

@Preview
@Composable
private fun UnselectedSettingsRowDarkThemePreview() {
    WordifyTheme(darkTheme = true) {
        SettingsRow(isSelected = false, settingsValue = "a -> z", onSelectValue = {})
    }
}

@Preview
@Composable
private fun SelectedSettingsRowLightThemePreview() {
    WordifyTheme(darkTheme = false) {
        SettingsRow(isSelected = true, settingsValue = "a -> z", onSelectValue = {})
    }
}

@Preview
@Composable
private fun UnselectedSettingsRoLightThemePreview() {
    WordifyTheme(darkTheme = false) {
        SettingsRow(isSelected = false, settingsValue = "a -> z", onSelectValue = {})
    }
}
