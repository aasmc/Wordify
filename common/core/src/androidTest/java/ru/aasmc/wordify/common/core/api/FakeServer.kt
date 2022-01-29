package ru.aasmc.wordify.common.core.api

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import ru.aasmc.constants.ApiConstants
import ru.aasmc.wordify.Logger
import java.io.InputStream

class FakeServer {
    private val mockWebServer = MockWebServer()
    private val notFoundResponse = MockResponse().setResponseCode(404)
    private val endPointSeparator = "/"
    private val wordEndPointPath = "$endPointSeparator${ApiConstants.WORD_ENDPOINT}"

    val baseEndPoint
        get() = mockWebServer.url(endPointSeparator)

    fun start() {
        mockWebServer.start(8080)
    }

    fun setHappyPathDispatcher() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val path = request.path ?: return notFoundResponse

                return with(path) {
                    when {
                        startsWith(wordEndPointPath) -> {
                            MockResponse().setResponseCode(200).setBody(getJson("track.json"))
                        }
                        else -> notFoundResponse
                    }
                }
            }
        }
    }

    fun shutDown() {
        mockWebServer.shutdown()
    }

    private fun getJson(path: String): String {
        return try {
            val context = InstrumentationRegistry.getInstrumentation().context
            val jsonStream: InputStream = context.assets.open("networkresponses/$path")
            String(jsonStream.readBytes())
        } catch (e: Exception) {
            Logger.e(e, "Error reading network response json asset")
            throw e
        }
    }
}