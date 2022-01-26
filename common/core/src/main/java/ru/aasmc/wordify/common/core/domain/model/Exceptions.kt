package ru.aasmc.wordify.common.core.domain.model

import okio.IOException

class NetworkUnavailableException(message: String = "Sorry! No network connection...") :
    IOException(message)