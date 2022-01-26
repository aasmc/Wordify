package ru.aasmc.wordify.common.core.data.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.aasmc.wordify.common.core.data.api.ConnectionManager
import ru.aasmc.wordify.common.core.domain.model.NetworkUnavailableException
import javax.inject.Inject

class NetworkStatusInterceptor @Inject constructor(
    private val connectionManager: ConnectionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (connectionManager.isConnected) {
            chain.proceed(chain.request())
        } else {
            throw NetworkUnavailableException()
        }
    }

}