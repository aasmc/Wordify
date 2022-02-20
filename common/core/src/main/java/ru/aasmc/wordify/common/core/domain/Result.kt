package ru.aasmc.wordify.common.core.domain

import ru.aasmc.wordify.common.core.utils.ProgressBarState

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val reason: String) : Result<Nothing>()
    data class Loading(val progressBarState: ProgressBarState = ProgressBarState.IDLE): Result<Nothing>()
}
