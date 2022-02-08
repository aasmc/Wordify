package ru.aasmc.wordify

import android.app.Application
import android.content.Context
import androidx.compose.runtime.MutableState
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.aasmc.wordify.common.core.domain.repositories.Sort
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import ru.aasmc.wordify.features.settings.domain.usecases.GetAppThemeFlow
import ru.aasmc.wordify.features.settings.domain.usecases.GetSortOrderFlow
import javax.inject.Inject

@HiltAndroidApp
class WordifyApp : Application() {
    @Inject
    lateinit var getAppThemeFlow: GetAppThemeFlow

    @Inject
    lateinit var getSortOrderFlow: GetSortOrderFlow

    private val _appThemeState: MutableStateFlow<ThemePreference> =
        MutableStateFlow(ThemePreference.AUTO_THEME)
    val appThemeState: StateFlow<ThemePreference> = _appThemeState.asStateFlow()

    private val _sortOrderState: MutableStateFlow<Sort> = MutableStateFlow(Sort.ASC_NAME)
    val sortOrderState: StateFlow<Sort> = _sortOrderState.asStateFlow()

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {

            launch {
                getAppThemeFlow().collectLatest {
                    _appThemeState.value = it
                }
            }
            launch {
                getSortOrderFlow().collectLatest {
                    _sortOrderState.value = it
                }
            }
        }
    }
}


val Context.appThemeFlow: StateFlow<ThemePreference>
    get() = (this as WordifyApp).appThemeState

val Context.sortOrderFlow: StateFlow<Sort>
    get() = (this as WordifyApp).sortOrderState