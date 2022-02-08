package ru.aasmc.wordify.common.core.domain.repositories

enum class Sort {
    ASC_NAME, DESC_NAME, ASC_TIME, DESC_TIME;

    companion object {
        fun fromOrdinal(ordinal: Int) = values()[ordinal]
    }
}