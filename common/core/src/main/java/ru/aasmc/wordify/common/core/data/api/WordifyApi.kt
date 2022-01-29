package ru.aasmc.wordify.common.core.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.aasmc.constants.ApiConstants
import ru.aasmc.wordify.common.core.data.api.model.WordDto

interface WordifyApi {

    @GET("${ApiConstants.WORD_ENDPOINT}/{word}")
    suspend fun getWord(
        @Path("word") word: String
    ): WordDto

}