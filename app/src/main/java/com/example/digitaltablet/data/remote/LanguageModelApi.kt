package com.example.digitaltablet.data.remote

import com.example.digitaltablet.domain.model.llm.common.FileObj
import com.example.digitaltablet.domain.model.llm.AssistantList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface LanguageModelApi {

    @GET("assistants")
    suspend fun listAssistants(
        @Header("Authorization") apiKey: String
    ): AssistantList

    @Multipart
    @POST("files")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("purpose") purpose: RequestBody,
        @Header("Authorization") apiKey: String
    ): FileObj
}