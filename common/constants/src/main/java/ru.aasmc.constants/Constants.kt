package ru.aasmc.constants

object ApiParameters {
    const val RAPID_API_HOST_HEADER = "x-rapidapi-host"
    const val RAPID_API_HOST_VALUE = "wordsapiv1.p.rapidapi.com"
    const val RAPID_API_KEY_HEADER = "X-RapidAPI-Key"
    // TODO remove real key after publishing
}

object ApiConstants {
    const val WORD_ENDPOINT = "words"
    const val BASE_URL = "https://wordsapiv1.p.rapidapi.com/"

    const val NETWORK_TIMEOUT = 5000L
}

object CacheConstants {
    const val CACHE_TIMEOUT = 2000L
    const val PAGE_SIZE = 30
    const val MAX_SIZE = 200
}

object ExceptionMessage {
    const val FAILURE_TO_GET_WORD_FROM_NETWORK = "Failed to get word from network!"
    const val FAILURE_TO_GET_WORD_FROM_CACHE = "Failed to get word from cache!"
    const val CACHE_TIMEOUT_ERROR = "Cache timeout error!"
    const val NETWORK_TIMEOUT_ERROR = "Network timeout error!"
    const val UNKNOWN_ERROR = "Sorry! Something went wrong!"
    const val FAILED_TO_SET_WORD_FAVOURITE = "Sorry! Something went wrong when changing word favourite status"
}

object PreferencesConstants {
    const val DATASTORE_NAME = "wordify_datastore"
    const val SHARED_PREFS_NAME = "wordify_datastore"
}

object WordConstants {
    const val WORD_ID_ARGUMENT = "wordId"
    const val WORD_TITLE_TAG = "word_title_tag"
}

object SettingsTestTags {
    const val RADIO_BUTTON_TAG = "radio_button_tag"
}

object WordDetailsTestTags {
    const val WORD_LOADING_TAG = "word_loading_tag"
    const val WORD_PROPERTY_ROW_TAG = "word_property_row_tag"
}

object WordListConstants {
    const val WORD_ITEM_CARD_TAD = "word_item_card_tag"
}

object SearchConstants {
    const val SEARCH_SURFACE_TAG = "search_surface_tag"
    const val RECENTLY_SEARCHED_WORD_TAG = "recently_searched_word_tag"
}