package com.example.digitaltablet.data.remote

import com.example.digitaltablet.domain.model.llm.AssistantList
import retrofit2.http.GET
import retrofit2.http.Header

interface LanguageModelApi {

    @GET("assistants")
    suspend fun listAssistants(
        @Header("Authorization") apiKey: String
    ): AssistantList

}