package com.example.digitaltablet.data.repository

import com.example.digitaltablet.data.remote.LanguageModelApi
import com.example.digitaltablet.domain.model.llm.AssistantList
import com.example.digitaltablet.domain.repository.ILanguageModelRepository

class LanguageModelRepository(
    private val languageModelApi: LanguageModelApi
): ILanguageModelRepository {

    override suspend fun listAssistants(apiKey: String): AssistantList {
        return try {
            languageModelApi.listAssistants(
                apiKey = apiKey
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

}