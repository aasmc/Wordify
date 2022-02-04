package ru.aasmc.wordify.common.core.utils

import kotlinx.coroutines.*
import retrofit2.HttpException
import ru.aasmc.constants.ApiConstants
import ru.aasmc.constants.ExceptionMessage
import ru.aasmc.wordify.Logger
import ru.aasmc.wordify.common.core.domain.Result
import java.io.IOException

suspend inline fun <T : Any> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline body: suspend () -> T
): Result<T> {
    return withContext(dispatcher) {
        try {
//            val data = withTimeout(ApiConstants.NETWORK_TIMEOUT) {
//                body()
//            }
            Result.Success(body())
        } catch (e: Exception) {
            when (e) {
                is TimeoutCancellationException -> {
                    Logger.e(
                        e,
                        e.localizedMessage ?: ExceptionMessage.NETWORK_TIMEOUT_ERROR
                    )
                    Result.Failure(
                        "${ExceptionMessage.NETWORK_TIMEOUT_ERROR} Reason: ${e.localizedMessage}"
                    )
                }
                is IOException -> {
                    Logger.e(
                        e,
                        e.localizedMessage ?: ExceptionMessage.FAILURE_TO_GET_WORD_FROM_NETWORK
                    )
                    Result.Failure(
                        "${ExceptionMessage.FAILURE_TO_GET_WORD_FROM_NETWORK} Reason: ${e.localizedMessage}"
                    )
                }
                else -> {
                    Logger.e(e, e.localizedMessage ?: ExceptionMessage.UNKNOWN_ERROR)
                    Result.Failure(
                        "${ExceptionMessage.UNKNOWN_ERROR} Reason: ${e.localizedMessage}"
                    )
                }
            }
        }
    }
}