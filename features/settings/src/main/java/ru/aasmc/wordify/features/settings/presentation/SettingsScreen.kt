package ru.aasmc.wordify.features.settings.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import ru.aasmc.wordify.features.settings.R
import ru.aasmc.wordify.resources.theme.WordifyTheme

@Composable
fun SettingsScreen(
    title: String,
    appTheme: ThemePreference,
    sortOrder: Sort,
    viewModel: PreferencesViewModel
) {
    val sortFilters = listOf(
        RadioButtonItem(id = Sort.ASC_NAME.ordinal, title = stringResource(id = R.string.sort_name_asc)) ,
        RadioButtonItem(id = Sort.DESC_NAME.ordinal, title = stringResource(id = R.string.sort_name_desc)) ,
        RadioButtonItem(id = Sort.ASC_TIME.ordinal, title = stringResource(id = R.string.sort_time_added_asc)) ,
        RadioButtonItem(id = Sort.DESC_TIME.ordinal, title = stringResource(id = R.string.sort_time_added_desc))
    )
    val themeFilters = listOf(
        RadioButtonItem(id = ThemePreference.AUTO_THEME.ordinal, title = stringResource(id = R.string.theme_auto)),
        RadioButtonItem(id = ThemePreference.DARK_THEME.ordinal, title = stringResource(id = R.string.theme_dark)),
        RadioButtonItem(id = ThemePreference.LIGHT_THEME.ordinal, title = stringResource(id = R.string.theme_light))
    )

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
                selectedValue = sortOrder.ordinal,
                onSelectedValueChange = { sortOrdinal ->
                    viewModel.handleEvent(
                        UserPrefsEvent.ChangeSortOrder(
                            sortOrder = Sort.fromOrdinal(sortOrdinal)
                        )
                    )
                },
            )
            // theme selection
            SettingsCard(
                label = stringResource(id = R.string.theme_selection_title),
                settingsValues = themeFilters,
                selectedValue = appTheme.ordinal,
                onSelectedValueChange = { themeOrdinal ->
                    viewModel.handleEvent(
                        UserPrefsEvent.ChangeTheme(
                            themePreference = ThemePreference.fromOrdinal(themeOrdinal)
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
            sortOrder = Sort.ASC_TIME
        )
    }
}

@Composable
private fun SettingsCard(
    label: String,
    settingsValues: Iterable<RadioButtonItem>,
    selectedValue: Int,
    onSelectedValueChange: (Int) -> Unit
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
            settingsValues = listOf(
                RadioButtonItem(1, "a -> z"),
                RadioButtonItem(2, "z -> a"),
                RadioButtonItem(3, "Time added asc"),
                RadioButtonItem(4, "Time added desc"),
            ),
            selectedValue = 1,
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
            settingsValues = listOf(
                RadioButtonItem(1, "a -> z"),
                RadioButtonItem(2, "z -> a"),
                RadioButtonItem(3, "Time added asc"),
                RadioButtonItem(4, "Time added desc"),
            ),
            selectedValue = 1,
            onSelectedValueChange = {}
        )
    }
}

@Composable
private fun SettingsColumn(
    settingsValues: Iterable<RadioButtonItem>,
    selectedValue: Int,
    onSelectedValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .selectableGroup(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        settingsValues.forEach { radioItem ->
            SettingsRow(
                item = radioItem,
                isSelected = selectedValue == radioItem.id,
                onSelectValue = { onSelectedValueChange(radioItem.id) }
            )
        }
    }
}

@Preview
@Composable
private fun SettingsColumnPreviewDarkTheme() {
    WordifyTheme(darkTheme = true) {
        SettingsColumn(
            settingsValues = listOf(
                RadioButtonItem(1, "a -> z"),
                RadioButtonItem(2, "z -> a")
            ),
            selectedValue = 1,
            onSelectedValueChange = {}
        )
    }
}

@Preview
@Composable
private fun SettingsColumnPreviewLightTheme() {
    WordifyTheme(darkTheme = false) {
        SettingsColumn(
            settingsValues = listOf(
                RadioButtonItem(1, "a -> z"),
                RadioButtonItem(2, "z -> a")
            ),
            selectedValue = 1,
            onSelectedValueChange = {}
        )
    }
}

data class RadioButtonItem(
    val id: Int,
    val title: String
)

@Composable
private fun SettingsRow(
    item: RadioButtonItem,
    isSelected: Boolean,
    onSelectValue: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .selectable(
                selected = isSelected,
                onClick = { onSelectValue(item.id) },
                role = Role.RadioButton
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = MaterialTheme.colors.secondary
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = item.title,
            style = MaterialTheme.typography.body2,
        )
    }
}

@Preview
@Composable
private fun SelectedSettingsRowDarkThemePreview() {
    WordifyTheme(darkTheme = true) {
        SettingsRow(
            isSelected = true,
            item = RadioButtonItem(id = 1, title = "a -> z"),
            onSelectValue = {})
    }
}

@Preview
@Composable
private fun UnselectedSettingsRowDarkThemePreview() {
    WordifyTheme(darkTheme = true) {
        SettingsRow(
            isSelected = false,
            item = RadioButtonItem(id = 1, title = "a -> z"),
            onSelectValue = {})
    }
}

@Preview
@Composable
private fun SelectedSettingsRowLightThemePreview() {
    WordifyTheme(darkTheme = false) {
        SettingsRow(
            isSelected = true,
            item = RadioButtonItem(id = 1, title = "a -> z"),
            onSelectValue = {})
    }
}

@Preview
@Composable
private fun UnselectedSettingsRoLightThemePreview() {
    WordifyTheme(darkTheme = false) {
        SettingsRow(
            isSelected = false,
            item = RadioButtonItem(id = 1, title = "a -> z"),
            onSelectValue = {})
    }
}
