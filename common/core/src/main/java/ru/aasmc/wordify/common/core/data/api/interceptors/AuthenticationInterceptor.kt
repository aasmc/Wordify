package ru.aasmc.wordify.common.core.data.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.aasmc.constants.ApiParameters
import ru.aasmc.wordify.common.core.BuildConfig
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val hostHeader = ApiParameters.RAPID_API_HOST_HEADER
        val hostHeaderValue = ApiParameters.RAPID_API_HOST_VALUE
        val apiKeyHeader = ApiParameters.RAPID_API_KEY_HEADER
        val apiKey = BuildConfig.ApiKey

        val request = chain.request()
            .newBuilder()
            .addHeader(hostHeader, hostHeaderValue)
            .addHeader(apiKeyHeader, apiKey)
            .build()
        return chain.proceed(request)
    }
}