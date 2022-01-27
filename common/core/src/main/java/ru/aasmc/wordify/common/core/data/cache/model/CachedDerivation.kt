package ru.aasmc.wordify.common.core.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cachedDerivation")
data class CachedDerivation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "derivationId")
    val derivationId: Long = 0,
    @ColumnInfo(name = "derivation")
    val derivation: String
)
