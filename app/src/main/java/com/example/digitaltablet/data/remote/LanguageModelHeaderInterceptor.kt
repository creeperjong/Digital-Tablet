package com.example.digitaltablet.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class LanguageModelHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalAuthToken = originalRequest.header("Authorization")
        val modifiedAuthToken = originalAuthToken?.let { "Bearer $it" }
        val newRequest = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("OpenAI-Beta", "assistants=v2")
            .header("Authorization", modifiedAuthToken ?: "")
            .build()
        return chain.proceed(newRequest)
    }
}