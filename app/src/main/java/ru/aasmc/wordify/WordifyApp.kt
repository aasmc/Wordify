package ru.aasmc.wordify

import android.app.Application
import android.content.Context
import androidx.compose.runtime.MutableState
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.aasmc.wordify.common.core.domain.repositories.ThemePreference
import ru.aasmc.wordify.features.settings.domain.usecases.GetAppThemeFlow
import javax.inject.Inject

@HiltAndroidApp
class WordifyApp : Application() {

}
