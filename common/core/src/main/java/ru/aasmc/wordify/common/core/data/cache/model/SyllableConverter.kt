package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.TypeConverter

class SyllableConverter {
    @TypeConverter
    fun fromSyllables(syllables: List<String>): String {
        return syllables.joinToString(separator = ",")
    }
    @TypeConverter
    fun toSyllables(syllableStr: String): List<String> {
        return syllableStr.split(",")
    }
}