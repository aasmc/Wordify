package ru.aasmc.wordify.common.core.domain

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val reason: String) : Result<Nothing>()
}
