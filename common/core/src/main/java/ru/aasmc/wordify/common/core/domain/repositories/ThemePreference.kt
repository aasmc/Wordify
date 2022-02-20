package ru.aasmc.wordify.common.core.domain.repositories

enum class ThemePreference {
    DARK_THEME, LIGHT_THEME, AUTO_THEME;

    fun shouldUseDarkTheme(isSystemInDarkMode: Boolean): Boolean {
        return when(this) {
            DARK_THEME -> true
            LIGHT_THEME -> false
            AUTO_THEME -> isSystemInDarkMode
        }
    }

    companion object {
        fun fromOrdinal(ordinal: Int) = values()[ordinal]
    }
}