package ru.aasmc.wordify.common.core.data.api.interceptors

import okhttp3.logging.HttpLoggingInterceptor
import ru.aasmc.wordify.Logger
import javax.inject.Inject

class LoggingInterceptor @Inject constructor() : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        Logger.i(message)
    }

}