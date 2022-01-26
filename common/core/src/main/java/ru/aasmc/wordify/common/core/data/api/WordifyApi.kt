package ru.aasmc.wordify.common.core.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.aasmc.wordify.common.core.data.api.model.WordDto

interface WordifyApi {

    @GET("{word}")
    suspend fun getWord(
        @Path("word") word: String
    ): WordDto

}