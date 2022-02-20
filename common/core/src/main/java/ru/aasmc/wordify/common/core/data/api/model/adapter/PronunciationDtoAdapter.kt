package ru.aasmc.wordify.common.core.data.api.model.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson
import ru.aasmc.wordify.common.core.data.api.model.PronunciationDto

class PronunciationDtoAdapter {
    /**
     * This method is unused since I don't POST anything to web.
     */
    @ToJson
    fun toJson(pronunciation: PronunciationDto): String {
        return pronunciation.toString()
    }

    @FromJson
    fun fromJson(reader: JsonReader): PronunciationDto = with(reader) {
        var all: String? = null
        if (hasNext()) {
            when (peek()) {
                JsonReader.Token.BEGIN_OBJECT -> {
                    beginObject()
                    if (hasNext()) {
                        if (nextName() == "all") {
                            if (peek() != JsonReader.Token.NULL) {
                                all = nextString()
                            }
                        }
                    }
                    endObject()
                }
                JsonReader.Token.STRING -> {
                    all = nextString()
                }
                else -> skipValue()
            }
        }
        PronunciationDto(
            all
        )
    }
}





















