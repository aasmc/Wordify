package ru.aasmc.wordify.common.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import ru.aasmc.constants.CacheConstants
import ru.aasmc.constants.ExceptionMessage
import ru.aasmc.wordify.Logger
import ru.aasmc.wordify.common.core.domain.Result

suspend inline fun <T : Any> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    crossinline cacheCall: suspend () -> T?
): Result<T> {
    return withContext(dispatcher) {
        try {
            val cacheResult = cacheCall()
            if (cacheResult == null) {
                Result.Failure(ExceptionMessage.FAILURE_TO_GET_WORD_FROM_CACHE)
            } else {
                Result.Success(cacheResult)
            }
        } catch (t: Throwable) {
            when (t) {
                is TimeoutCancellationException -> {
                    Logger.e(t, t.localizedMessage ?: ExceptionMessage.CACHE_TIMEOUT_ERROR)
                    Result.Failure("${ExceptionMessage.CACHE_TIMEOUT_ERROR} ${t.localizedMessage}")
                }
                else -> {
                    Logger.e(t, t.localizedMessage ?: ExceptionMessage.UNKNOWN_ERROR)
                    Result.Failure("${ExceptionMessage.UNKNOWN_ERROR} ${t.localizedMessage}")
                }
            }
        }
    }
}