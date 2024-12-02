package com.example.digitaltablet.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class LanguageModelHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalAuthToken = originalRequest.header("Authorization")
        val modifiedAuthToken = originalAuthToken?.let { "Bearer $it" }

        val contentType = if (hasFileId(originalRequest.body)) {
            "multipart/form-data"
        } else "application/json"

        val newRequest = originalRequest.newBuilder()
            .addHeader("Content-Type", contentType)
            .addHeader("OpenAI-Beta", "assistants=v2")
            .header("Authorization", modifiedAuthToken ?: "")
            .build()
        return chain.proceed(newRequest)
    }

    private fun hasFileId(requestBody: RequestBody?): Boolean {
        if (requestBody is MultipartBody) {
            return requestBody.parts.any { part ->
                part.headers
                    ?.get("Content-Disposition")
                    ?.contains("name=\"file\"") == true
            }
        }
        return false
    }
}