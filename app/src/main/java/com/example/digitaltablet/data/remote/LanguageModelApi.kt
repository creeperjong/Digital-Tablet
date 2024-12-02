package com.example.digitaltablet.data.remote

import com.example.digitaltablet.domain.model.llm.common.FileObj
import com.example.digitaltablet.domain.model.llm.AssistantList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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

    @GET("files/{fileId}/content")
    suspend fun retrieveFileContent(
        @Path("fileId") fileId: String,
        @Header("Authorization") apiKey: String
    ): Response<ResponseBody>
}